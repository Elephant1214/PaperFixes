package me.elephant1214.paperfixes.mixin.common.server.improved_tick_loop;

import me.elephant1214.paperfixes.PaperFixes;
import me.elephant1214.paperfixes.configuration.PaperFixesConfig;
import net.minecraft.command.ICommandSender;
import net.minecraft.crash.CrashReport;
import net.minecraft.network.ServerStatusResponse;
import net.minecraft.profiler.ISnooperInfo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.ReportedException;
import net.minecraft.util.Util;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.StartupQuery;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.*;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.FutureTask;
import java.util.concurrent.locks.LockSupport;

import static me.elephant1214.paperfixes.util.TickConstants.*;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin implements ICommandSender, Runnable, IThreadListener, ISnooperInfo {
    @Shadow
    @Final
    private static Logger LOGGER;
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
    @Final
    public Queue<FutureTask<?>> futureTaskQueue;

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

    /**
     * True when a task was run, false when disabled or when there are no tasks to run.
     * This is basically to take some of the load off of tick and not have it run the
     * entire queue.
     */
    @SuppressWarnings("SynchronizeOnNonFinalField")
    @Unique
    private boolean paperFixes$tryRunTasks() {
        if (!PaperFixesConfig.features.runTasksDuringSleep) return false;

        synchronized (this.futureTaskQueue) {
            if (!this.futureTaskQueue.isEmpty()) {
                Util.runTask((FutureTask<?>) this.futureTaskQueue.poll(), LOGGER);
                return true;
            }
        }

        return false;
    }

    /**
     * @author Elephant_1214
     * @reason Overwrites run to add the improved tick loop; injecting it is not possible.
     */
    @Overwrite(remap = false)
    public void run() {
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY); // Settings the thread priority to maximum helps performance on some systems

        try {
            if (!this.init()) {
                FMLCommonHandler.instance().expectServerStopped();
                this.finalTick(null);
                return;
            }

            PaperFixes.LOGGER.info("Using PaperFixes' improved tick loop, spin time {} ns", PaperFixesConfig.features.tickLoopSpinTime);
            if (PaperFixesConfig.features.runTasksDuringSleep) {
                PaperFixes.LOGGER.info("Using PaperFixes' improved task scheduler");
            }

            FMLCommonHandler.instance().handleServerStarted();
            this.currentTime = System.currentTimeMillis(); // See the doc on the shadowed field

            this.statusResponse.setServerDescription(new TextComponentString(this.motd));
            this.statusResponse.setVersion(new ServerStatusResponse.Version("1.12.2", 340));
            this.applyServerIconToResponse(this.statusResponse);

            long nextTickStart = System.nanoTime();
            // Stops the server from reporting an overload in console on startup
            long lastOverloadWarning = nextTickStart + OVERLOADED_THRESHOLD + OVERLOADED_WARNING_INTERVAL;
            while (this.serverRunning) {
                long now = System.nanoTime();
                long nanosBehind = now - nextTickStart;
                if (nanosBehind > OVERLOADED_THRESHOLD) {
                    this.paperFixes$catchupTicks = nanosBehind / NANOS_PER_TICK;

                    if (now - lastOverloadWarning >= OVERLOADED_WARNING_INTERVAL) {
                        LOGGER.warn("Multiple ticks took too long! Attempting to catch up by {} ticks ({} ms)", this.paperFixes$catchupTicks, nanosBehind / NANOS_PER_MILLI);
                        lastOverloadWarning = now;
                    }
                }

                nextTickStart += NANOS_PER_TICK;

                this.tick(); // Tick the game
                this.currentTime = System.currentTimeMillis(); // See the doc on the shadowed field

                // If the server is catching up, remove the current tick from the count
                if (this.paperFixes$catchupTicks > 0L) {
                    this.paperFixes$catchupTicks -= 1L;
                }

                if (this.paperFixes$catchupTicks == 0L) {
                    while ((now = System.nanoTime()) < nextTickStart) {
                        long remaining = nextTickStart - now;
                        if (remaining > (long) PaperFixesConfig.features.tickLoopSpinTime) {
                            if (!this.paperFixes$tryRunTasks()) {
                                LockSupport.parkNanos(remaining); // Park nanos tends to unpark about 70-90 micros early
                            }
                        } else {
                            // spin
                        }
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
