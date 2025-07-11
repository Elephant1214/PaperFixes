package me.elephant1214.paperfixes.mixin.common.block.shulker_dupe;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BlockShulkerBox.class, priority = 500)
public abstract class BlockShulkerBoxMixin extends BlockContainer {
    protected BlockShulkerBoxMixin(Material material, MapColor mapColor) {
        super(material, mapColor);
    }

    @Inject(method = "breakBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockShulkerBox;spawnAsEntity(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/item/ItemStack;)V", shift = At.Shift.AFTER))
    private void fixShulkerDupe(World worldIn, BlockPos pos, IBlockState state, CallbackInfo ci, @Local(ordinal = 0) TileEntityShulkerBox tileEntityShulkerBox) {
        tileEntityShulkerBox.clear();
    }
}
