package me.elephant1214.paperfixes.mixin.common.entity.passive;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityWaterMob.class)
public abstract class MixinEntityWaterMob extends EntityLiving implements IAnimals {
    private MixinEntityWaterMob(World worldIn) {
        super(worldIn);
    }

    @Inject(
            method = "getCanSpawnHere",
            at = @At("HEAD"),
            cancellable = true
    )
    private void fixCanSpawnHere(CallbackInfoReturnable<Boolean> cir) {
        final int x = MathHelper.floor(this.posX);
        final int y = MathHelper.floor(this.getEntityBoundingBox().minY);
        final int z = MathHelper.floor(this.posZ);
        Block block = this.world.getBlockState(new BlockPos(x, y, z)).getBlock();

        cir.setReturnValue(block == Blocks.WATER || block == Blocks.FLOWING_WATER);
        cir.cancel();
    }
}
