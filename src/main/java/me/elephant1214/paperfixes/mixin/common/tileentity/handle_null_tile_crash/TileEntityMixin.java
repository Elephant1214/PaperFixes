package me.elephant1214.paperfixes.mixin.common.tileentity.handle_null_tile_crash;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import me.elephant1214.paperfixes.PaperFixes;
import net.minecraft.block.Block;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TileEntity.class)
public abstract class TileEntityMixin implements ICapabilitySerializable<NBTTagCompound> {
    @WrapWithCondition(method = "addInfoToCrashReport", at = @At(value = "INVOKE", target = "Lnet/minecraft/crash/CrashReportCategory;addBlockInfo(Lnet/minecraft/crash/CrashReportCategory;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;I)V"))
    private boolean preventTeCrashes(CrashReportCategory category, BlockPos pos, @Nullable Block block, int metadata) {
        if (block == null) {
            PaperFixes.LOGGER.error("A tile entity with a null block was found at {}. It will be excluded from the crash report to prevent additional issues.", pos.toString());
            return false;
        }
        return true;
    }
}
