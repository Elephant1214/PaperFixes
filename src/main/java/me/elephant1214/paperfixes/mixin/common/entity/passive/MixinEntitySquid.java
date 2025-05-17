package me.elephant1214.paperfixes.mixin.common.entity.passive;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Random;

@Mixin(EntitySquid.class)
public abstract class MixinEntitySquid extends EntityWaterMob {
    private MixinEntitySquid(World world) {
        super(world);
    }

    @WrapWithCondition(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/util/Random;setSeed(J)V"))
    private boolean ignoreSetSeed(Random instance, long seed) {
        return false;
    }
}
