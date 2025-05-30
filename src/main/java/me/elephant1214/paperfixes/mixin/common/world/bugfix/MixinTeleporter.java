package me.elephant1214.paperfixes.mixin.common.world.bugfix;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.Teleporter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Teleporter.class)
public abstract class MixinTeleporter {
    @Inject(method = "placeInExistingPortal", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/NetHandlerPlayServer;setPlayerLocation(DDDFF)V"))
    private void mc98153(Entity entityIn, float rotationYaw, CallbackInfoReturnable<Boolean> cir) {
        ((InvokerNetHandlerPlayServer) ((EntityPlayerMP) entityIn).connection).invokeCaptureCurrentPosition();
    }
}
