package me.elephant1214.paperfixes.util;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import org.jetbrains.annotations.Nullable;

public class CachedChunkProviderHashMap extends Long2ObjectOpenHashMap<Chunk> {
    private @Nullable Chunk lastAccessedChunk = null;

    public CachedChunkProviderHashMap() {
        super(8192);
    }

    @Override
    public Chunk get(long key) {
        if (lastAccessedChunk != null && key == asChunkKey(lastAccessedChunk)) {
            return lastAccessedChunk;
        }
        return lastAccessedChunk = super.get(key);
    }

    @Override
    public Chunk remove(long key) {
        if (lastAccessedChunk != null && key == asChunkKey(lastAccessedChunk)) {
            lastAccessedChunk = null;
        }
        return super.remove(key);
    }

    private static long asChunkKey(Chunk chunk) {
        return ChunkPos.asLong(chunk.x, chunk.z);
    }
}
