package me.elephant1214.paperfixes.mixin.common;

import me.elephant1214.paperfixes.PaperFixes;
import net.minecraft.block.BlockMobSpawner;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(Chunk.class)
public abstract class MixinChunk {
    @Shadow
    @Final
    private Map<BlockPos, TileEntity> tileEntities;

    @Inject(
            method = "addTileEntity(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/tileentity/TileEntity;)V",
            at = @At("RETURN")
    )
    private void removeInvalidMobSpawners(BlockPos pos, TileEntity tileEntityIn, CallbackInfo ci) {
        if (tileEntityIn instanceof TileEntityMobSpawner && !(((Chunk) (Object) this).getBlockState(pos).getBlock() instanceof BlockMobSpawner)) {
            PaperFixes.LOGGER.warn("Removed invalid mob spawner at " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ());
            this.tileEntities.remove(pos);
        }
    }
}
