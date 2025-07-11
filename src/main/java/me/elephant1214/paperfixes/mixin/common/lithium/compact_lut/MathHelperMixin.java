package me.elephant1214.paperfixes.mixin.common.lithium.compact_lut;

import net.caffeinemc.lithium.util.math.CompactSineLUT;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MathHelper.class)
public abstract class MathHelperMixin {
    @Shadow
    @Final
    @Mutable
    public static float[] SIN_TABLE;

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void initMathHelper(CallbackInfo ci) {
        CompactSineLUT.init(); // Force static initialization
        MathHelperMixin.SIN_TABLE = null;
    }

    /**
     * @author jellysquid3 originally, Elephant_1214
     * @reason Use an optimized implementation
     */
    @Overwrite
    public static float sin(float value) {
        return CompactSineLUT.sin(value);
    }

    /**
     * @author jellysquid3 originally, Elephant_1214
     * @reason Use an optimized implementation
     */
    @Overwrite
    public static float cos(float value) {
        return CompactSineLUT.cos(value);
    }
}
