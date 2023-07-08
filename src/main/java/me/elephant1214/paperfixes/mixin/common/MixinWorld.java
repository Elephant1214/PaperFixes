package me.elephant1214.paperfixes.mixin.common;

import me.elephant1214.paperfixes.PaperFixes;
import me.elephant1214.paperfixes.configuration.PaperFixesConfig;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Iterator;

@Mixin(World.class)
public abstract class MixinWorld {
    private final boolean keepSpawnInMemory = PaperFixesConfig.keepSpawnInMemory;

    @Redirect(
            method = "updateEntities",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Iterator;next()Ljava/lang/Object;",
                    opcode = 1
            )
    )
    private Object paperfixes_handleNullTileEntities(Iterator<TileEntity> iterator) {
        TileEntity tileEntity = iterator.next();
        if (tileEntity == null) {
            PaperFixes.LOGGER.error("A null tile entity was detected and removed!");
            iterator.remove();
            return iterator.next();
        }
        return tileEntity;
    }

    @Inject(
            method = "isSpawnChunk",
            at = @At("HEAD"),
            cancellable = true
    )
    private void paperfixes_keepSpawnInMemory(int x, int z, CallbackInfoReturnable<Boolean> cir) {
        if (!this.keepSpawnInMemory) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
