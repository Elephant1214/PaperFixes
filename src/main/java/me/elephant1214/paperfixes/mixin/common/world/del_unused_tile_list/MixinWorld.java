package me.elephant1214.paperfixes.mixin.common.world.del_unused_tile_list;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Collection;
import java.util.List;

@SuppressWarnings("unused")
@Mixin(World.class)
public abstract class MixinWorld implements IBlockAccess, ICapabilityProvider {
    @Shadow
    public final List<TileEntity> loadedTileEntityList = null;
    @Shadow
    @Final
    public List<TileEntity> tickableTileEntities;

    @Shadow
    public abstract boolean addTileEntity(TileEntity tile);

    /**
     * Skip the call to removeAll, return value is irrelevant.
     */
    @WrapOperation(method = "updateEntities", at = @At(value = "INVOKE", target = "Ljava/util/List;removeAll(Ljava/util/Collection;)Z", remap = false, ordinal = 2))
    private boolean skipRemoveAll(@Nullable List<TileEntity> instance, Collection<TileEntity> objects, Operation<Boolean> original) {
        return true;
    }

    /**
     * Skip the call to contains, return value must be false otherwise it won't be added.
     */
    @WrapOperation(method = "updateEntities", at = @At(value = "INVOKE", target = "Ljava/util/List;contains(Ljava/lang/Object;)Z", remap = false))
    private boolean skipContains(@Nullable List<TileEntity> instance, Object object, Operation<Boolean> original) {
        return false;
    }

    /**
     * Skip the remove call, return value is irrelevant.
     */
    @WrapOperation(method = "updateEntities", at = @At(value = "INVOKE", target = "Ljava/util/List;remove(Ljava/lang/Object;)Z", remap = false))
    private boolean skipRemove(@Nullable List<TileEntity> instance, Object object, Operation<Boolean> original) {
        return true;
    }

    /**
     * Skip the call to "add", return true since we essentially want to ignore the local
     * variable "flag".
     */
    @WrapOperation(method = "addTileEntity", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", remap = false, ordinal = 1))
    private boolean flagAlwaysTrue(@Nullable List<TileEntity> instance, Object object, Operation<Boolean> original) {
        return true;
    }

    /**
     * Because loadedTileEntityList was removed,
     * this check needs to be modified to ensure tickableTileEntities doesn't contain the tile.
     */
    @Definition(id = "ITickable", type = ITickable.class)
    @Expression("? instanceof ITickable")
    @ModifyExpressionValue(method = "addTileEntity", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean updateCheck(boolean original, @Local(argsOnly = true) TileEntity tile) {
        return original && !this.tickableTileEntities.contains(tile);
    }

    /**
     * Skip the remove call for non-tickable tiles, return value is irrelevant.
     */
    @WrapOperation(method = "removeTileEntity", at = @At(value = "INVOKE", target = "Ljava/util/List;remove(Ljava/lang/Object;)Z", remap = false, ordinal = 1))
    private boolean skipNonTickableRemoval(@Nullable List<TileEntity> instance, Object object, Operation<Boolean> original) {
        return true;
    }

    /**
     * Skip the remove call for tickable tiles, the return value is irrelevant.
     */
    @Redirect(method = "removeTileEntity", at = @At(value = "INVOKE", target = "Ljava/util/List;remove(Ljava/lang/Object;)Z", remap = false, ordinal = 3))
    private boolean skipTickableRemoval(@Nullable List<TileEntity> instance, Object object) {
        return true;
    }
}
