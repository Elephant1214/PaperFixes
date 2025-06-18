package me.elephant1214.paperfixes.mixin.common.world.fast_border;

import me.elephant1214.paperfixes.manager.FastWorldBorder;
import net.minecraft.world.border.WorldBorder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldBorder.class)
public abstract class MixinWorldBorder {
    @Inject(method = "minX", at = @At("HEAD"), cancellable = true)
    private void useCachedMinX(CallbackInfoReturnable<Double> cir) {
        cir.setReturnValue(FastWorldBorder.instance().minX());
        cir.cancel();
    }

    @Inject(method = "minZ", at = @At("HEAD"), cancellable = true)
    private void useCachedMinZ(CallbackInfoReturnable<Double> cir) {
        cir.setReturnValue(FastWorldBorder.instance().minZ());
        cir.cancel();
    }

    @Inject(method = "maxX", at = @At("HEAD"), cancellable = true)
    private void useCachedMaxX(CallbackInfoReturnable<Double> cir) {
        cir.setReturnValue(FastWorldBorder.instance().maxX());
        cir.cancel();
    }

    @Inject(method = "maxZ", at = @At("HEAD"), cancellable = true)
    private void useCachedMaxZ(CallbackInfoReturnable<Double> cir) {
        cir.setReturnValue(FastWorldBorder.instance().maxZ());
        cir.cancel();
    }
}
