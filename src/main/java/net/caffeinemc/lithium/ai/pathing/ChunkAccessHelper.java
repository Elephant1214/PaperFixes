package net.caffeinemc.lithium.ai.pathing;

import net.minecraft.world.chunk.Chunk;
import org.jetbrains.annotations.Nullable;

public interface ChunkAccessHelper {
    @Nullable Chunk paperFixes$getChunkAt(int x, int z);
}
