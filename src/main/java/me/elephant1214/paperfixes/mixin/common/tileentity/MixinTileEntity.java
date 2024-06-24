package me.elephant1214.paperfixes.mixin.common.tileentity;

import net.minecraft.block.Block;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TileEntity.class)
public abstract class MixinTileEntity implements ICapabilitySerializable<NBTTagCompound> {
    @Redirect(
            method = "addInfoToCrashReport",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/crash/CrashReportCategory;addBlockInfo(Lnet/minecraft/crash/CrashReportCategory;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;I)V"
            )
    )
    private void preventTeCrashes(CrashReportCategory category, BlockPos pos, Block block, int metadata) {
        if (block != null) {
            CrashReportCategory.addBlockInfo(category, pos, block, metadata);
        }
    }
}
