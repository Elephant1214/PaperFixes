package me.elephant1214.paperfixes.mixin.common.network.invoker;

import net.minecraft.network.NetHandlerPlayServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(NetHandlerPlayServer.class)
public interface InvokerNetHandlerPlayServer {
    @Invoker
    void invokeCaptureCurrentPosition();
}
