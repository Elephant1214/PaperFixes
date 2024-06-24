package me.elephant1214.paperfixes.mixin.common.entity;

import net.minecraft.entity.ai.attributes.BaseAttribute;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RangedAttribute.class)
public abstract class MixinRangedAttribute extends BaseAttribute {
    private MixinRangedAttribute(@Nullable IAttribute iAttribute, String string, double d) {
        super(iAttribute, string, d);
    }

    @Inject(method = "clampValue", at = @At("HEAD"), cancellable = true)
    private void mc133373(double value, CallbackInfoReturnable<Double> cir) {
        if (value != value) {
            cir.setReturnValue(this.getDefaultValue());
            cir.cancel();
        }
    }
}
