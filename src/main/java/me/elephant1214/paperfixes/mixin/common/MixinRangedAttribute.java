package me.elephant1214.paperfixes.mixin.common;

import net.minecraft.entity.ai.attributes.RangedAttribute;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RangedAttribute.class)
public class MixinRangedAttribute {
    @Inject(method = "clampValue", at = @At("HEAD"), cancellable = true)
    private void mc133373(double value, CallbackInfoReturnable<Double> cir) {
        if (value != value) {
            cir.setReturnValue(((RangedAttribute) (Object) this).getDefaultValue());
            cir.cancel();
        }
    }
}
