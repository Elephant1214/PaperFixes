package me.elephant1214.paperfixes.mixin.common.item;

import me.elephant1214.paperfixes.mixin.common.nbt.accessor.AccessorNBTTagList;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Comparator;

@Mixin(ItemStack.class)
public class MixinItemStack {
    @Unique
    private static final Comparator<NBTTagCompound> ENCHANT_SORTER = Comparator.comparingInt(c -> c.getInteger("id"));
    @Shadow
    private NBTTagCompound stackTagCompound;

    @Inject(
            method = "<init>(Lnet/minecraft/nbt/NBTTagCompound;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/nbt/NBTTagCompound;getCompoundTag(Ljava/lang/String;)Lnet/minecraft/nbt/NBTTagCompound;",
                    shift = At.Shift.AFTER,
                    ordinal = 1
            )
    )
    private void constructorFixEnchantOrder(NBTTagCompound compound, CallbackInfo ci) {
        this.paperFixes$fixEnchantOrder(this.stackTagCompound);
    }

    @Inject(
            method = "setTagCompound",
            at = @At("TAIL")
    )
    private void setTagFixEnchantOrder(NBTTagCompound nbt, CallbackInfo ci) {
        this.paperFixes$fixEnchantOrder(this.stackTagCompound);
    }

    @Inject(
            method = "addEnchantment",
            at = @At("TAIL")
    )
    private void addEnchantFixEnchantOrder(Enchantment ench, int level, CallbackInfo ci) {
        this.paperFixes$fixEnchantOrder(this.stackTagCompound);
    }

    @SuppressWarnings("unchecked")
    @Unique
    private void paperFixes$fixEnchantOrder(NBTTagCompound tag) {
        if (tag == null || !tag.hasKey("ench", 9)) return;
        NBTTagList enchants = tag.getTagList("ench", 10);
        if (enchants.tagCount() < 2) return;
        try {
            ((AccessorNBTTagList) enchants).getTagList().sort((Comparator<NBTBase>) (Comparator<? extends NBTBase>) MixinItemStack.ENCHANT_SORTER);
        } catch (Exception ignored) {
        }
    }
}
