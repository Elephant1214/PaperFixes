package me.elephant1214.paperfixes.mixin.server.world.cache_border;

import me.elephant1214.paperfixes.manager.SmartWorldBorder;
import me.elephant1214.paperfixes.util.MixinUpdateHook;
import net.minecraft.world.border.WorldBorder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldBorder.class)
public abstract class MixinWorldBorder implements MixinUpdateHook {
    @Shadow
    public abstract double getDiameter();

    @Unique
    private final SmartWorldBorder paperFixes$smarterBorder = new SmartWorldBorder((WorldBorder) (Object) this);

    @Override
    public void paperFixes$update() {
        this.getDiameter(); // Used to force the game to send out a border size update, if needed
    }

    @Inject(method = "setCenter", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/border/WorldBorder;getListeners()Ljava/util/List;"))
    private void updateBorderForCenter(double x, double z, CallbackInfo ci) {
        this.paperFixes$smarterBorder.recompute();
    }

    @Inject(method = "setTransition(D)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/border/WorldBorder;getListeners()Ljava/util/List;"))
    private void updateForTransition(double newSize, CallbackInfo ci) {
        System.out.println("Transition");
        this.paperFixes$smarterBorder.recompute();
    }

    @Inject(method = "setTransition(DDJ)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/border/WorldBorder;getListeners()Ljava/util/List;"))
    private void updateForTransitionTime(double oldSize, double newSize, long time, CallbackInfo ci) {
        this.paperFixes$smarterBorder.recompute();
    }

    @Inject(method = "minX", at = @At("HEAD"), cancellable = true)
    private void useCachedMinX(CallbackInfoReturnable<Double> cir) {
        cir.setReturnValue(this.paperFixes$smarterBorder.minX());
        cir.cancel();
    }

    @Inject(method = "minZ", at = @At("HEAD"), cancellable = true)
    private void useCachedMinZ(CallbackInfoReturnable<Double> cir) {
        cir.setReturnValue(this.paperFixes$smarterBorder.minZ());
        cir.cancel();
    }

    @Inject(method = "maxX", at = @At("HEAD"), cancellable = true)
    private void useCachedMaxX(CallbackInfoReturnable<Double> cir) {
        cir.setReturnValue(this.paperFixes$smarterBorder.maxX());
        cir.cancel();
    }

    @Inject(method = "maxZ", at = @At("HEAD"), cancellable = true)
    private void useCachedMaxZ(CallbackInfoReturnable<Double> cir) {
        cir.setReturnValue(this.paperFixes$smarterBorder.maxZ());
        cir.cancel();
    }
}
