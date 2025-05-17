package me.elephant1214.paperfixes.mixin.common.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(BlockShulkerBox.class)
public abstract class MixinBlockShulkerBox extends BlockContainer {
    protected MixinBlockShulkerBox(Material material, MapColor mapColor) {
        super(material, mapColor);
    }

    @SuppressWarnings("InvalidInjectorMethodSignature")
    @Inject(
            method = "breakBlock",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockShulkerBox;spawnAsEntity(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/item/ItemStack;)V",
                    shift = At.Shift.AFTER
            ),
            locals = LocalCapture.CAPTURE_FAILSOFT
    )
    private void fixShulkerDupe(World worldIn, BlockPos pos, IBlockState state, CallbackInfo ci, TileEntity tileEntity, TileEntityShulkerBox tileEntityShulkerBox) {
        tileEntityShulkerBox.clear();
    }
}
