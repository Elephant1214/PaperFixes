package me.elephant1214.paperfixes.mixin.common.world.cache_densities;

import me.elephant1214.paperfixes.manager.ExplosionDensityCacheManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.profiler.ISnooperInfo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IThreadListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin implements ICommandSender, Runnable, IThreadListener, ISnooperInfo {
    @Inject(method = "stopServer", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;info(Ljava/lang/String;)V", shift = At.Shift.AFTER, ordinal = 0, remap = false))
    private void clearCacheOnStop(CallbackInfo ci) {
        ExplosionDensityCacheManager.INSTANCE.clearCache();
    }

    @Inject(method = "reload", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/management/PlayerList;saveAllPlayerData()V"))
    private void clearCacheOnReload(CallbackInfo ci) {
        ExplosionDensityCacheManager.INSTANCE.clearCache();
    }
}
