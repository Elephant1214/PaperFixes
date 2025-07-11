package me.elephant1214.paperfixes.mixin.common.lithium.path_node_cache;

import net.caffeinemc.lithium.ai.pathing.ChunkAccessHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkCache.class)
public class ChunkCacheMixin implements ChunkAccessHelper {
    @Shadow
    protected Chunk[][] chunkArray;

    @Shadow
    protected int chunkX;

    @Shadow
    protected int chunkZ;

    // The x/z length of this cache
    @Unique
    private int paperFixes$xLen, paperFixes$zLen;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(World worldIn, BlockPos minPos, BlockPos maxPos, int subIn, CallbackInfo ci) {
        this.paperFixes$xLen = 1 + (maxPos.getX() >> 4) - (minPos.getX() >> 4);
        this.paperFixes$zLen = 1 + (maxPos.getZ() >> 4) - (minPos.getZ() >> 4);
    }

    @Override
    public @Nullable Chunk paperFixes$getChunkAt(int x, int z) {
        int chunkX = x - this.chunkX;
        int chunkZ = z - this.chunkZ;
        if (chunkX >= 0 && chunkX < this.paperFixes$xLen && chunkZ >= 0 && chunkZ < this.paperFixes$zLen) {
            return this.chunkArray[chunkX][chunkZ];
        } else {
            return null;
        }
    }
}
