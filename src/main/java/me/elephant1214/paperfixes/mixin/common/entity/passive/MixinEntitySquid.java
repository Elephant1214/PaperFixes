package me.elephant1214.paperfixes.mixin.common.entity.passive;

import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

@Mixin(EntitySquid.class)
public abstract class MixinEntitySquid extends EntityWaterMob {
    private MixinEntitySquid(World world) {
        super(world);
    }

    @Redirect(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Random;setSeed(J)V"
            )
    )
    private void ignoreSetSeed(Random instance, long seed) {
    }
}
