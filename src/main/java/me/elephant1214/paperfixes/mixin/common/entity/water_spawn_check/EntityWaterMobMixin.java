package me.elephant1214.paperfixes.mixin.common.entity.water_spawn_check;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
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

@Mixin(EntityWaterMob.class)
public abstract class EntityWaterMobMixin extends EntityLiving implements IAnimals {
    private EntityWaterMobMixin(World worldIn) {
        super(worldIn);
    }

    @ModifyReturnValue(method = "getCanSpawnHere", at = @At("RETURN"))
    private boolean fixCanSpawnHere(boolean original) {
        final int x = MathHelper.floor(this.posX);
        final int y = MathHelper.floor(this.getEntityBoundingBox().minY);
        final int z = MathHelper.floor(this.posZ);
        Block block = this.world.getBlockState(new BlockPos(x, y, z)).getBlock();

        return block == Blocks.WATER || block == Blocks.FLOWING_WATER;
    }
}
