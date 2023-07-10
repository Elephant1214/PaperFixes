package me.elephant1214.paperfixes.mixin.common;

import io.netty.channel.Channel;
import net.minecraft.network.NetworkManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Queue;

@Mixin(NetworkManager.class)
public class MixinNetworkManager {
    @SuppressWarnings("rawtypes")
    @Shadow
    @Final
    private Queue outboundPacketsQueue;
    @Shadow
    private Channel channel;

    @Inject(method = "checkDisconnected", at = @At("RETURN"))
    private void paperfixes_clearPacketQueueOnDisconnect(CallbackInfo ci) {
        if (this.channel != null && !this.channel.isOpen() && !this.outboundPacketsQueue.isEmpty()) {
            this.outboundPacketsQueue.clear();
        }
    }
}
