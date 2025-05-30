package me.elephant1214.paperfixes.mixin.common.world.bugfix;

import net.minecraft.entity.Entity;
import net.minecraft.network.NetHandlerPlayServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * See {@link MixinTeleporter#mc98153(Entity, float, CallbackInfoReturnable)}
 */
@Mixin(NetHandlerPlayServer.class)
public interface InvokerNetHandlerPlayServer {
    @Invoker
    void invokeCaptureCurrentPosition();
}
