package me.elephant1214.paperfixes.mixin.common.server;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.command.ICommandSender;
import net.minecraft.profiler.ISnooperInfo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IThreadListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MinecraftServer.class)
public abstract class MixinMinecraftServer_TaskScheduler implements ICommandSender, Runnable, IThreadListener, ISnooperInfo {
    @ModifyExpressionValue(
            method = "updateTimeLightAndEntities",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Queue;isEmpty()Z"
            ),
            allow = 1
    )
    private boolean skipTasksInTickLoop(boolean original) {
        return true;
    }
}
