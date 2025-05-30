package me.elephant1214.paperfixes.mixin.common.entity.optimize_pathfinding;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.elephant1214.paperfixes.PaperFixes;
import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(PathNavigate.class)
public abstract class MixinPathNavigate {
    @Shadow
    protected @Nullable Path currentPath;

    @Unique
    private int paperFixes$pathfindFailures = 0;
    @Unique
    private int paperFixes$lastFailure = 0;

    @Inject(method = "tryMoveToEntityLiving", at = @At("HEAD"), cancellable = true)
    private void dontRetryIfNoPathAndFailures(Entity entityIn, double speedIn, CallbackInfoReturnable<Boolean> cir) {
        if (this.paperFixes$pathfindFailures > 10 && this.currentPath == null && PaperFixes.getServer().getTickCounter() < this.paperFixes$lastFailure + 40) {
            cir.cancel();
            cir.setReturnValue(false);
        }
    }

    @ModifyReturnValue(method = "tryMoveToEntityLiving", at = @At("RETURN"))
    private boolean optimizePathfinding(boolean original) {
        if (original) {
            this.paperFixes$lastFailure = 0;
            this.paperFixes$pathfindFailures = 0;
            return true;
        } else {
            this.paperFixes$pathfindFailures++;
            this.paperFixes$lastFailure = PaperFixes.getServer().getTickCounter();
            return false;
        }
    }

    @Inject(method = "clearPath", at = @At("HEAD"))
    private void clearFailures(CallbackInfo ci) {
        this.paperFixes$pathfindFailures = 0;
        this.paperFixes$lastFailure = 0;
    }
}
