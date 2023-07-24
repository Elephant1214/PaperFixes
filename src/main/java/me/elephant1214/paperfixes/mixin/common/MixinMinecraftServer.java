package me.elephant1214.paperfixes.mixin.common;

import me.elephant1214.paperfixes.PaperFixes;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer {
    @Inject(
            method = "updateTimeLightAndEntities",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/profiler/Profiler;endSection()V",
                    ordinal = 3
            ),
            allow = 1
    )
    private void paperfixes_clearExplosionDensityCache(CallbackInfo ci) {
        PaperFixes.explosionDensityCache.clear();
    }
}
