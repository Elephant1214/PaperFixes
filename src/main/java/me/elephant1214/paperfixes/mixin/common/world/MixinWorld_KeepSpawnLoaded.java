package me.elephant1214.paperfixes.mixin.common.world;

import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public abstract class MixinWorld_KeepSpawnLoaded implements IBlockAccess, ICapabilityProvider {
    @Inject(method = "isSpawnChunk", at = @At("HEAD"), cancellable = true)
    private void keepSpawnLoaded(int x, int z, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
        cir.cancel();
    }
}
