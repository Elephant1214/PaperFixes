package me.elephant1214.paperfixes.mixin.common.server.optimize_task_queue;

import io.papermc.paper.CachedSizeConcurrentLinkedQueue;
import net.minecraft.command.ICommandSender;
import net.minecraft.profiler.ISnooperInfo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IThreadListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Queue;
import java.util.concurrent.FutureTask;

@SuppressWarnings("unused")
@Mixin(MinecraftServer.class)
public abstract class MixinMinecraftServer implements ICommandSender, Runnable, IThreadListener, ISnooperInfo {
    @Shadow
    public final Queue<FutureTask<?>> futureTaskQueue = new CachedSizeConcurrentLinkedQueue<>();
}
