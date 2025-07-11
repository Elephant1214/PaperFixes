package me.elephant1214.paperfixes.mixin.common.entity.bugfix;

import net.minecraft.entity.ai.attributes.BaseAttribute;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RangedAttribute.class)
public abstract class RangedAttributeMixin extends BaseAttribute {
    private RangedAttributeMixin(@Nullable IAttribute iAttribute, String string, double value) {
        super(iAttribute, string, value);
    }

    @Inject(method = "clampValue", at = @At("HEAD"), cancellable = true)
    private void mc133373(double value, CallbackInfoReturnable<Double> cir) {
        if (Double.isNaN(value)) {
            cir.setReturnValue(this.getDefaultValue());
            cir.cancel();
        }
    }
}
