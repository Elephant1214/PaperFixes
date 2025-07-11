package me.elephant1214.paperfixes.mixin.common.init.dispenser_shulker_crash;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Cancellable;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.init.Bootstrap$BehaviorDispenseShulkerBox")
public class Bootstrap$BehaviorDispenseShulkerBoxMixin {
    @ModifyExpressionValue(method = "dispenseStack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;)Z"))
    private boolean fixDispenseCrash(boolean original, IBlockSource source, ItemStack stack, @Cancellable CallbackInfoReturnable<ItemStack> cir) {
        if (!original) {
            cir.setReturnValue(stack);
            cir.cancel();
        }

        return original;
    }
}
