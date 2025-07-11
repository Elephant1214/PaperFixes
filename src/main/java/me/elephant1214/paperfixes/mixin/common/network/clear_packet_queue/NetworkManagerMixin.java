package me.elephant1214.paperfixes.mixin.common.network.clear_packet_queue;

import io.netty.channel.Channel;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Queue;

@Mixin(NetworkManager.class)
public abstract class NetworkManagerMixin extends SimpleChannelInboundHandler<Packet<?>> {
    @Shadow
    @Final
    private Queue<NetworkManager.InboundHandlerTuplePacketListener> outboundPacketsQueue;
    @Shadow
    private Channel channel;

    @Inject(method = "handleDisconnection", at = @At("RETURN"))
    private void clearPacketQueueOnDisconnect(CallbackInfo ci) {
        if (this.channel != null && !this.channel.isOpen() && !this.outboundPacketsQueue.isEmpty()) {
            this.outboundPacketsQueue.clear();
        }
    }
}
