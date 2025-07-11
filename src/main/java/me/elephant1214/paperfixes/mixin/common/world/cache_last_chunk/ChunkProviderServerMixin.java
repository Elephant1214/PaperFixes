package me.elephant1214.paperfixes.mixin.common.world.cache_last_chunk;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import me.elephant1214.paperfixes.util.CachedChunkProviderHashMap;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@SuppressWarnings("unused")
@Mixin(ChunkProviderServer.class)
public abstract class ChunkProviderServerMixin implements IChunkProvider {
    @Shadow
    public final Long2ObjectMap<Chunk> loadedChunks = new CachedChunkProviderHashMap();
}
