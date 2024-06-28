package me.elephant1214.paperfixes.mixin.common.server;

import net.minecraft.command.ICommandSender;
import net.minecraft.profiler.ISnooperInfo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IThreadListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.elephant1214.paperfixes.PaperFixes.explosionDensityCache;

@Mixin(MinecraftServer.class)
public abstract class MixinMinecraftServer_ExplosionDensity implements ICommandSender, Runnable, IThreadListener, ISnooperInfo {
    @Inject(
            method = "stopServer",
            at = @At(value = "INVOKE",
                    target = "Lorg/apache/logging/log4j/Logger;info(Ljava/lang/String;)V",
                    shift = At.Shift.AFTER,
                    ordinal = 0,
                    remap = false
            )
    )
    private void clearExplosionDensityCache(CallbackInfo ci) {
        explosionDensityCache.clearCache();
    }
}
