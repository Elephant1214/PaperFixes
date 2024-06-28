package me.elephant1214.paperfixes.mixin.common.world.chunk.storage;

import io.papermc.paper.QueuedChunk;
import me.elephant1214.paperfixes.PaperFixes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.storage.IThreadedFileIO;
import net.minecraft.world.storage.ThreadedFileIOBase;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

@Mixin(AnvilChunkLoader.class)
public abstract class MixinAnvilChunkLoader implements IChunkLoader, IThreadedFileIO {
    @Unique private final ConcurrentLinkedQueue<QueuedChunk> paperFixes$queue = new ConcurrentLinkedQueue<>();
    @Unique private final Object paperFixes$lock = new Object();
    @Unique private final AtomicLong paperFixes$processedSaves = new AtomicLong(0L);
    
    @Shadow @Final private Map<ChunkPos, NBTTagCompound> chunksToSave;
    @Shadow @Final public File chunkSaveLocation;

    @Shadow protected abstract void writeChunkData(ChunkPos pos, NBTTagCompound compound) throws IOException;

    @Shadow @Final protected static Logger LOGGER;

    @Inject(
            method = "addChunkToPending",
            at = @At("HEAD"),
            cancellable = true
    )
    protected void addToPendingQueue(ChunkPos pos, NBTTagCompound compound, CallbackInfo ci) {
        synchronized (this.paperFixes$lock) {
            this.chunksToSave.put(pos, compound);
        }
        
        this.paperFixes$queue.add(new QueuedChunk(pos, compound));

        ThreadedFileIOBase.getThreadedIOInstance().queueIO(this);
        ci.cancel();
    }

    @Inject(
            method = "writeNextIO",
            at = @At("HEAD"),
            cancellable = true
    )
    private void callNewWriteMethod(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(this.paperFixes$processSaveQueueEntry(false));
        cir.cancel();
    }
    
    @Inject(
            method = "flush",
            at = @At("HEAD"),
            cancellable = true
    )
    private void useNewSaveProcessing(CallbackInfo ci) {
        while (true) {
            if (!this.paperFixes$processSaveQueueEntry(true)) break;
        }
        ci.cancel();
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
