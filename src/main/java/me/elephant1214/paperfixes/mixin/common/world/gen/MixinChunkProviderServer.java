package me.elephant1214.paperfixes.mixin.common.world.gen;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@SuppressWarnings("unused")
@Mixin(ChunkProviderServer.class)
public abstract class MixinChunkProviderServer implements IChunkProvider {
    @Unique
    protected Chunk paperFixes$lastAccessedChunk = null;

    @Shadow
    public final Long2ObjectMap<Chunk> loadedChunks = new Long2ObjectOpenHashMap<Chunk>(8192) {
        @Override
        public Chunk get(long key) {
            if (paperFixes$lastAccessedChunk != null && key == paperFixes$cachedAsLong()) {
                return paperFixes$lastAccessedChunk;
            }
            return paperFixes$lastAccessedChunk = super.get(key);
        }

        @Override
        public Chunk remove(long key) {
            if (paperFixes$lastAccessedChunk != null && key == paperFixes$cachedAsLong()) {
                paperFixes$lastAccessedChunk = null;
            }
            return super.remove(key);
        }
    };

    /**
     * Should only be called when this.paperFixes$lastAccessedChunk is *NOT* null
     */
    @Unique
    private long paperFixes$cachedAsLong() {
        return ChunkPos.asLong(this.paperFixes$lastAccessedChunk.x, this.paperFixes$lastAccessedChunk.z);
    }
}
