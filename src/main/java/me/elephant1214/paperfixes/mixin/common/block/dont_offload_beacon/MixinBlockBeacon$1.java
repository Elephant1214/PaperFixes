package me.elephant1214.paperfixes.mixin.common.block.dont_offload_beacon;

import com.google.common.util.concurrent.ListenableFuture;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "net.minecraft.block.BlockBeacon$1")
public abstract class MixinBlockBeacon$1 implements Runnable {
    @Redirect(method = "run", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldServer;addScheduledTask(Ljava/lang/Runnable;)Lcom/google/common/util/concurrent/ListenableFuture;"), remap = false)
    private ListenableFuture<Void> runColorUpdateNow(WorldServer instance, Runnable runnableToSchedule) {
        runnableToSchedule.run();
        return null;
    }
}
