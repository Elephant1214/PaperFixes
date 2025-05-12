package me.elephant1214.paperfixes.mixin.common.server;

import me.elephant1214.paperfixes.PaperFixes;
import me.elephant1214.paperfixes.configuration.PaperFixesConfig;
import me.elephant1214.paperfixes.configuration.TickLoopMode;
import net.minecraft.command.ICommandSender;
import net.minecraft.crash.CrashReport;
import net.minecraft.network.ServerStatusResponse;
import net.minecraft.profiler.ISnooperInfo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.ReportedException;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.StartupQuery;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.*;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static me.elephant1214.paperfixes.util.TickConstants.*;

@Mixin(MinecraftServer.class)
public abstract class MixinMinecraftServer implements ICommandSender, Runnable, IThreadListener, ISnooperInfo {
    @Shadow
    @Final
    protected static Logger LOGGER;
    @Shadow
    protected long currentTime;
    @Shadow
    @Final
    private ServerStatusResponse statusResponse;
    @Shadow
    private boolean serverRunning;
    @Shadow
    private boolean serverStopped;
    @Shadow
    private String motd;
    @Shadow
    private boolean serverIsRunning;

    @Unique
    private static long paperFixes$getNanos() {
        return System.nanoTime();
    }

    @Shadow
    public abstract boolean init() throws IOException;

    @Shadow
    public abstract void applyServerIconToResponse(ServerStatusResponse response);

    @Shadow
    public abstract void tick();

    @Shadow
    public abstract void finalTick(CrashReport report);

    @Shadow
    public abstract CrashReport addServerInfoToCrashReport(CrashReport report);

    @Shadow
    public abstract File getDataDirectory();

    @Shadow
    public abstract void stopServer();

    @Shadow
    public abstract void systemExitNow();

    @Unique
    private long paperFixes$catchupTicks = 0L;
    @Unique
    private boolean paperFixes$forceTicks = false;
    @Unique
    private long paperFixes$nextTickTime = 0L;
    @Unique
    private long paperFixes$lastOverloadWarning = 0L;

    @Unique
    private boolean paperFixes$canSleep() {
        return paperFixes$getNanos() < this.paperFixes$nextTickTime;
    }

    @Unique
    private long paperFixes$calculateSleepTime() {
        return this.paperFixes$nextTickTime - paperFixes$getNanos();
    }

    @Unique
    private void paperFixes$sleepUntilNextTick() throws InterruptedException {
        if (this.paperFixes$forceTicks) return;
        if (paperFixes$canSleep()) {
            final long sleepTime = paperFixes$calculateSleepTime() / NANOS_PER_SECOND;
            Thread.sleep(sleepTime);
        }
    }

    /**
     * @author Elephant_1214
     * @reason Manually injecting this is not possible.
     */
    @Overwrite(remap = false)
    public void run() {
        try {
            if (this.init()) {
                TickLoopMode tickLoopMode = PaperFixesConfig.INSTANCE.enhancedTickLoopMode;
                PaperFixes.LOGGER.info("Using PaperFixes' enhanced tick loop, option: {}", tickLoopMode == TickLoopMode.DYNAMIC_SLEEP_TIME ? "Dynamic sleep time" : "Keep TPS at or above 19");

                FMLCommonHandler.instance().handleServerStarted();
                this.currentTime = MinecraftServer.getCurrentTimeMillis();

                this.statusResponse.setServerDescription(new TextComponentString(this.motd));
                this.statusResponse.setVersion(new ServerStatusResponse.Version("1.12.2", 340));
                this.applyServerIconToResponse(this.statusResponse);

                this.paperFixes$nextTickTime = paperFixes$getNanos();

                while (this.serverRunning) {
                    long timeToNext = paperFixes$getNanos() - this.paperFixes$nextTickTime;
                    long ticksBehind = timeToNext / NANOS_PER_TICK;

                    if (tickLoopMode == TickLoopMode.KEEP_TPS_AT_OR_ABOVE_19 && ticksBehind > 1 && !this.paperFixes$forceTicks) {
                        this.paperFixes$forceTicks = true;
                        this.paperFixes$catchupTicks = ticksBehind;
                    }

                    if (timeToNext > OVERLOADED_THRESHOLD + TICKS_PER_SECOND * NANOS_PER_TICK) {
                        if (this.paperFixes$nextTickTime - this.paperFixes$lastOverloadWarning >= OVERLOADED_WARNING_INTERVAL + 100L * NANOS_PER_TICK) {
                            LOGGER.warn("Can't keep up! Is the server overloaded? Running {}ms or {} ticks behind", timeToNext / NANOS_PER_MILLI, ticksBehind);
                            this.paperFixes$nextTickTime += ticksBehind * NANOS_PER_TICK;
                            this.paperFixes$lastOverloadWarning = this.paperFixes$nextTickTime;
                        }

                        if (tickLoopMode == TickLoopMode.DYNAMIC_SLEEP_TIME) {
                            this.paperFixes$forceTicks = true;
                            this.paperFixes$catchupTicks = ticksBehind;
                        }
                    }

                    this.paperFixes$nextTickTime += NANOS_PER_TICK;
                    this.currentTime = MinecraftServer.getCurrentTimeMillis();
                    this.tick();

                    if (this.paperFixes$forceTicks && paperFixes$catchupTicks > 0) {
                        this.paperFixes$nextTickTime = paperFixes$getNanos();
                        if (--this.paperFixes$catchupTicks == 0) {
                            this.paperFixes$forceTicks = false;
                        }
                    }

                    this.paperFixes$sleepUntilNextTick();
                    this.serverIsRunning = true;
                }

                FMLCommonHandler.instance().handleServerStopping();
                FMLCommonHandler.instance().expectServerStopped();
            } else {
                FMLCommonHandler.instance().expectServerStopped();
                this.finalTick(null);
            }
        } catch (StartupQuery.AbortedException ignored) {
            FMLCommonHandler.instance().expectServerStopped();
        } catch (Throwable unexpectedException) {
            LOGGER.error("Encountered an unexpected exception", unexpectedException);
            CrashReport crashreport;
            if (unexpectedException instanceof ReportedException) {
                crashreport = this.addServerInfoToCrashReport(((ReportedException) unexpectedException).getCrashReport());
            } else {
                crashreport = this.addServerInfoToCrashReport(new CrashReport("Exception in server tick loop", unexpectedException));
            }

            File crashReport = new File(new File(this.getDataDirectory(), "crash-reports"), "crash-" + new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date()) + "-server.txt");
            if (crashreport.saveToFile(crashReport)) {
                LOGGER.error("This crash report has been saved to: {}", crashReport.getAbsolutePath());
            } else {
                LOGGER.error("We were unable to save this crash report to disk.");
            }

            FMLCommonHandler.instance().expectServerStopped();
            this.finalTick(crashreport);
        } finally {
            try {
                this.stopServer();
            } catch (Throwable var68) {
                LOGGER.error("Exception stopping the server", var68);
            } finally {
                FMLCommonHandler.instance().handleServerStopped();
                this.serverStopped = true;
                this.systemExitNow();
            }
        }
    }
}
