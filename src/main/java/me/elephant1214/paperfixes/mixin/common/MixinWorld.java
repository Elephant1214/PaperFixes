package me.elephant1214.paperfixes.mixin.common;

import com.google.common.collect.Iterators;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.elephant1214.paperfixes.configuration.PaperFixesConfig;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Iterator;
import java.util.Objects;

@Mixin(World.class)
public abstract class MixinWorld {
    @ModifyExpressionValue(method = "updateEntities", at = @At(value = "INVOKE", target = "Ljava/util/List;iterator()Ljava/util/Iterator;", ordinal = 1))
    private Iterator<TileEntity> handleNullTileEntities(Iterator<TileEntity> instance) {
        return Iterators.filter(instance, Objects::nonNull);
    }

    @Inject(method = "isSpawnChunk", at = @At("HEAD"), cancellable = true)
    private void keepSpawnInMemory(int x, int z, CallbackInfoReturnable<Boolean> cir) {
        if (!PaperFixesConfig.keepSpawnInMemory) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
