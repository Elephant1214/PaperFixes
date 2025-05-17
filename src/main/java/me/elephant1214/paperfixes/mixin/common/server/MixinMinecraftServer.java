package me.elephant1214.paperfixes.mixin.common.server;

import me.elephant1214.paperfixes.PaperFixes;
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
import java.util.concurrent.locks.LockSupport;

import static java.lang.System.nanoTime;
import static me.elephant1214.paperfixes.util.TickConstants.*;

@Mixin(MinecraftServer.class)
public abstract class MixinMinecraftServer implements ICommandSender, Runnable, IThreadListener, ISnooperInfo {
    @Shadow
    @Final
    protected static Logger LOGGER;
    /**
     * Minecraft uses this field in the server watchdog thread to determine whether the main thread is hanging.
     * It must be updated every tick, or the watchdog thread will force the server to stop after about thirty seconds.
     */
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
    private long paperFixes$expectedNextTickStart = 0L;
    @Unique
    private long paperFixes$lastOverloadWarning = 0L;
    @Unique
    private long paperFixes$catchupTicks = 0L;

    /**
     * Calculates the allowed sleep time for the tick loop.
     * It should only ever sleep for the difference between the next tick time and right now.
     */
    @Unique
    private long paperFixes$calculateSleepTime() {
        return this.paperFixes$expectedNextTickStart - nanoTime();
    }

    /**
     * @author Elephant_1214
     * @reason Overwrites run to add the improved tick loop; injecting it is not possible.
     */
    @Overwrite(remap = false)
    public void run() {
        try {
            if (!this.init()) {
                FMLCommonHandler.instance().expectServerStopped();
                this.finalTick(null);
                return;
            }

            PaperFixes.LOGGER.info("Using PaperFixes' improved tick loop");

            FMLCommonHandler.instance().handleServerStarted();
            this.currentTime = System.currentTimeMillis(); // See the doc on the shadowed field

            this.statusResponse.setServerDescription(new TextComponentString(this.motd));
            this.statusResponse.setVersion(new ServerStatusResponse.Version("1.12.2", 340));
            this.applyServerIconToResponse(this.statusResponse);

            this.paperFixes$expectedNextTickStart = nanoTime();
            while (this.serverRunning) {
                this.tick();

                if (this.paperFixes$catchupTicks > 0L) {
                    this.paperFixes$catchupTicks -= 1L;
                }

                this.currentTime = System.currentTimeMillis();
                long nanosBehind = nanoTime() - this.paperFixes$expectedNextTickStart;
                if (nanosBehind > OVERLOADED_THRESHOLD && this.paperFixes$catchupTicks == 0L) {
                    this.paperFixes$catchupTicks = nanosBehind / NANOS_PER_TICK;

                    if (this.paperFixes$expectedNextTickStart - this.paperFixes$lastOverloadWarning >= OVERLOADED_WARNING_INTERVAL) {
                        LOGGER.warn("Multiple ticks took too long! Attempting to catch up by {} ticks ({} ms)", this.paperFixes$catchupTicks, nanosBehind / NANOS_PER_MILLI);
                        this.paperFixes$lastOverloadWarning = nanoTime();
                    }
                }

                this.paperFixes$expectedNextTickStart += NANOS_PER_TICK;

                if (this.paperFixes$catchupTicks == 0L) {
                    long sleepTime = this.paperFixes$calculateSleepTime();
                    if (sleepTime > 0L) {
                        Thread.yield();
                        LockSupport.parkNanos("waiting for tasks", sleepTime);
                    }
                }

                this.serverIsRunning = true; // Make sure the server is still marked as running
            }

            FMLCommonHandler.instance().handleServerStopping();
            FMLCommonHandler.instance().expectServerStopped();
        } catch (StartupQuery.AbortedException abort) {
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
