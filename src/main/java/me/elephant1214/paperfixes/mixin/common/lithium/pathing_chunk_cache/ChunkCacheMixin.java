package me.elephant1214.paperfixes.mixin.common.lithium.pathing_chunk_cache;

import net.caffeinemc.lithium.ai.pathing.ChunkAccessHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkCache.class)
public class ChunkCacheMixin implements ChunkAccessHelper {
    @Unique
    private static final IBlockState DEFAULT_BLOCK = Blocks.AIR.getDefaultState();

    @Shadow
    protected Chunk[][] chunkArray;

    @Shadow
    protected int chunkX;

    @Shadow
    protected int chunkZ;

    // A 1D view of the chunks available to this cache
    @Unique
    private Chunk[] paperFixes$chunksFlat;

    // The x/z length of this cache
    @Unique
    private int paperFixes$xLen, paperFixes$zLen;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(World worldIn, BlockPos minPos, BlockPos maxPos, int subIn, CallbackInfo ci) {
        this.paperFixes$xLen = 1 + (maxPos.getX() >> 4) - (minPos.getX() >> 4);
        this.paperFixes$zLen = 1 + (maxPos.getZ() >> 4) - (minPos.getZ() >> 4);

        this.paperFixes$chunksFlat = new Chunk[this.paperFixes$xLen * this.paperFixes$zLen];

        // Flatten the 2D chunk array into our 1D array
        for (int x = 0; x < this.paperFixes$xLen; x++) {
            System.arraycopy(this.chunkArray[x], 0, this.paperFixes$chunksFlat, x * this.paperFixes$zLen, this.paperFixes$zLen);
        }
    }

    /**
     * @author jellysquid3 originally, Elephant_1214
     * @reason Use optimized method
     */
    @Overwrite
    public @NotNull IBlockState getBlockState(@NotNull BlockPos pos) {
        int y = pos.getY();

        if (y >= 0 && y < 256) {
            int x = pos.getX();
            int z = pos.getZ();

            int chunkX = (x >> 4) - this.chunkX;
            int chunkZ = (z >> 4) - this.chunkZ;

            if (chunkX >= 0 && chunkX < this.paperFixes$xLen && chunkZ >= 0 && chunkZ < this.paperFixes$zLen) {
                Chunk chunk = this.paperFixes$chunksFlat[(chunkX * this.paperFixes$zLen) + chunkZ];

                // Avoid going through Chunk#getBlockState
                if (chunk != null) {
                    ExtendedBlockStorage subchunk = chunk.getBlockStorageArray()[y >> 4];

                    if (subchunk != null) {
                        return subchunk.get(x & 15, y & 15, z & 15);
                    }
                }
            }
        }

        return DEFAULT_BLOCK;
    }

    @Override
    public @Nullable Chunk paperFixes$getChunkAt(int x, int z) {
        int chunkX = x - this.chunkX;
        int chunkZ = z - this.chunkZ;
        if (chunkX >= 0 && chunkX < this.paperFixes$xLen && chunkZ >= 0 && chunkZ < this.paperFixes$zLen) {
            return this.paperFixes$chunksFlat[(chunkX * this.paperFixes$zLen) + chunkZ];
        } else {
            return null;
        }
    }
}
