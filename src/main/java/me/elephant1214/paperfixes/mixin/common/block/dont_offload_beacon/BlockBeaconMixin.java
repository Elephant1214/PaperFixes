package me.elephant1214.paperfixes.mixin.common.block.dont_offload_beacon;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import net.minecraft.block.BlockBeacon;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockBeacon.class)
public abstract class BlockBeaconMixin extends BlockContainer {
    protected BlockBeaconMixin(Material material, MapColor mapColor) {
        super(material, mapColor);
    }

    @Redirect(method = "updateColorAsync", at = @At(value = "INVOKE", target = "Lcom/google/common/util/concurrent/ListeningExecutorService;submit(Ljava/lang/Runnable;)Lcom/google/common/util/concurrent/ListenableFuture;", remap = false))
    private static ListenableFuture<Void> dontOffloadBeaconColorUpdate(ListeningExecutorService instance, Runnable runnable) {
        runnable.run();
        return null;
    }
}
