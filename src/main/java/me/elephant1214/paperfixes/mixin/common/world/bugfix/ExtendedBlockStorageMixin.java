package me.elephant1214.paperfixes.mixin.common.world.bugfix;

import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Decreased priority so that mods like Alfheim can overwrite this.
 */
@Mixin(value = ExtendedBlockStorage.class, priority = 500)
public abstract class ExtendedBlockStorageMixin {
    @Inject(method = "isEmpty", at = @At("HEAD"), cancellable = true)
    private void mc80966(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
        cir.cancel();
    }
}
