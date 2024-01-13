package me.elephant1214.paperfixes.mixin.common;

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
    @SuppressWarnings("unused")
    @ModifyExpressionValue(
            method = "updateEntities",
            at = @At(value = "INVOKE",
                    target = "Ljava/util/List;iterator()Ljava/util/Iterator;",
                    ordinal = 1)
    )
    private Iterator<TileEntity> handleNullTileEntities(Iterator<TileEntity> instance) {
        while (instance.hasNext()) {
            TileEntity element = instance.next();
            if (Objects.isNull(element)) {
                instance.remove();
            }
        }
        return instance;
    }

    @Inject(method = "isSpawnChunk", at = @At("HEAD"), cancellable = true)
    private void keepSpawnInMemory(int x, int z, CallbackInfoReturnable<Boolean> cir) {
        if (!PaperFixesConfig.keepSpawnInMemory) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
