package me.elephant1214.paperfixes.mixin.client.multiplayer.cache_last_chunk;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import me.elephant1214.paperfixes.util.CachedChunkProviderHashMap;
import net.minecraft.client.multiplayer.ChunkProviderClient;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@SuppressWarnings("unused")
@Mixin(ChunkProviderClient.class)
public abstract class MixinChunkProviderClient implements IChunkProvider {
    @Shadow
    private final Long2ObjectMap<Chunk> loadedChunks = new CachedChunkProviderHashMap() {
        @Override
        protected void rehash(int newN) {
            if (newN > this.key.length) {
                super.rehash(newN);
            }
        }
    };
}
