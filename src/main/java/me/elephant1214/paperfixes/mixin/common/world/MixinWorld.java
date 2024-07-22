package me.elephant1214.paperfixes.mixin.common.world;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Iterator;

@Mixin(World.class)
public abstract class MixinWorld implements IBlockAccess, ICapabilityProvider {
    @SuppressWarnings("unused")
    @ModifyExpressionValue(
            method = "updateEntities",
            at = @At(value = "INVOKE",
                    target = "Ljava/util/List;iterator()Ljava/util/Iterator;",
                    ordinal = 1,
                    remap = false
            )
    )
    private Iterator<TileEntity> handleNullTileEntities(Iterator<TileEntity> instance) {
        while (instance.hasNext()) {
            @Nullable TileEntity element = instance.next();
            if (null == element) {
                instance.remove();
            }
        }
        return instance;
    }
}
