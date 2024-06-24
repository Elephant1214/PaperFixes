package me.elephant1214.paperfixes.mixin.common.world.chunk;

import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ExtendedBlockStorage.class, priority = 500)
public abstract class MixinExtendedBlockStorage {
    @Inject(method = "isEmpty", at = @At("HEAD"), cancellable = true)
    private void mc80966(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
        cir.cancel();
    }
}
