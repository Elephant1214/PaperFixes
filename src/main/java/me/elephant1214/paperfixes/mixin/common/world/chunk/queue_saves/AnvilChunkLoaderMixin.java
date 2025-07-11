package me.elephant1214.paperfixes.mixin.common.world.chunk.queue_saves;

import io.papermc.paper.QueuedChunk;
import me.elephant1214.paperfixes.PaperFixes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.storage.IThreadedFileIO;
import net.minecraft.world.storage.ThreadedFileIOBase;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.*;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

@Mixin(AnvilChunkLoader.class)
public abstract class AnvilChunkLoaderMixin implements IChunkLoader, IThreadedFileIO {
    @Shadow
    @Final
    private static Logger LOGGER;
    @Unique
    private final ConcurrentLinkedQueue<QueuedChunk> paperFixes$queue = new ConcurrentLinkedQueue<>();
    @Unique
    private final Object paperFixes$lock = new Object();
    @Unique
    private final AtomicLong paperFixes$processedSaves = new AtomicLong(0L);
    @Shadow
    @Final
    public File chunkSaveLocation;
    @Shadow
    @Final
    private Map<ChunkPos, NBTTagCompound> chunksToSave;

    @Shadow
    protected abstract void writeChunkData(ChunkPos pos, NBTTagCompound compound) throws IOException;

    /**
     * @author Elephant_1214
     * @reason Queue world saving
     */
    @Overwrite
    protected void addChunkToPending(ChunkPos pos, NBTTagCompound compound) {
        synchronized (this.paperFixes$lock) {
            this.chunksToSave.put(pos, compound);
        }

        this.paperFixes$queue.add(new QueuedChunk(pos, compound));

        ThreadedFileIOBase.getThreadedIOInstance().queueIO(this);
    }

    /**
     * @author Elephant_1214
     * @reason Queue world saving
     */
    @Overwrite
    public boolean writeNextIO() {
        return this.paperFixes$processSaveQueueEntry(false);
    }

    /**
     * @author Elephant_1214
     * @reason Queue world saving
     */
    @Overwrite
    public void flush() {
        while (true) {
            if (!this.paperFixes$processSaveQueueEntry(true)) break;
        }
    }

    @Unique
    private synchronized boolean paperFixes$processSaveQueueEntry(boolean logCompletion) {
        QueuedChunk queuedChunk = this.paperFixes$queue.poll();
        if (queuedChunk == null) {
            if (logCompletion) {
                LOGGER.info("ThreadedAnvilChunkStorage ({}): All chunks are saved", this.chunkSaveLocation.getName());
            }

            return false;
        } else {
            ChunkPos pos = queuedChunk.pos;
            this.paperFixes$processedSaves.incrementAndGet();

            NBTTagCompound compound = queuedChunk.compound;
            int attempts = 0;
            Exception lastException = null;

            while (attempts++ < 5) {
                try {
                    this.writeChunkData(pos, compound);
                    lastException = null;
                    break;
                } catch (Exception e) {
                    lastException = e;
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    PaperFixes.LOGGER.error(e);
                }
            }

            if (lastException != null) {
                LOGGER.error("Failed to save chunk", lastException);
            }

            synchronized (this.paperFixes$lock) {
                if (this.chunksToSave.get(pos) == queuedChunk.compound) {
                    this.chunksToSave.remove(pos);
                }
            }

            return true;
        }
    }
}
