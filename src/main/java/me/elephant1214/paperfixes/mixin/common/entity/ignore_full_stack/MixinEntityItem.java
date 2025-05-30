package me.elephant1214.paperfixes.mixin.common.entity.ignore_full_stack;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityItem.class)
public abstract class MixinEntityItem extends Entity {
    @Shadow
    public abstract ItemStack getItem();

    public MixinEntityItem(World worldIn) {
        super(worldIn);
    }

    @Inject(method = "searchForOtherItemsNearby", at = @At("HEAD"), cancellable = true)
    private void avoidMergeForFullStacks(CallbackInfo ci) {
        ItemStack stack = this.getItem();
        if (stack.getCount() >= stack.getMaxStackSize()) {
            ci.cancel();
        }
    }
}
