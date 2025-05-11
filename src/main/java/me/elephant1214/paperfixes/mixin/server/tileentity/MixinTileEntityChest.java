package me.elephant1214.paperfixes.mixin.server.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntityChest.class)
public abstract class MixinTileEntityChest extends TileEntityLockableLoot implements ITickable {
    @Shadow
    public int numPlayersUsing;
    @Shadow
    public float lidAngle;
    @Shadow
    public TileEntityChest adjacentChestZNeg;
    @Shadow
    public TileEntityChest adjacentChestXNeg;
    @Shadow
    public TileEntityChest adjacentChestXPos;
    @Shadow
    public TileEntityChest adjacentChestZPos;

    @Shadow
    public abstract void checkForAdjacentChests();

    /**
     * @reason We never want this to run.
     * I'm not even sure what would happen if it did other than problems.
     */
    @Inject(method = "update", at = @At("HEAD"), cancellable = true)
    private void noChestAnimationInTick(CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(
            method = "openInventory",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;addBlockEvent(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;II)V"
            )
    )
    private void handleOpenChest(EntityPlayer player, CallbackInfo ci) {
        this.checkForAdjacentChests();

        if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F && this.adjacentChestZNeg == null && this.adjacentChestXNeg == null) {
            this.lidAngle = 0.7F;

            double d0 = (double) this.pos.getZ() + 0.5D;
            double d1 = (double) this.pos.getX() + 0.5D;

            if (this.adjacentChestZPos != null) {
                d0 += 0.5D;
            }

            if (this.adjacentChestXPos != null) {
                d1 += 0.5D;
            }

            this.world.playSound(null, d1, (double) this.pos.getY() + 0.5D, d0, SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
        }
    }

    @Inject(
            method = "closeInventory",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;addBlockEvent(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;II)V"
            )
    )
    private void handleCloseChest(EntityPlayer player, CallbackInfo ci) {
        if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F || this.numPlayersUsing > 0 && this.lidAngle < 1.0F) {
            float f = 0.1F;

            if (this.numPlayersUsing > 0) {
                this.lidAngle += f;
            } else {
                this.lidAngle -= f;
            }

            double d0 = (double) this.pos.getX() + 0.5D;
            double d2 = (double) this.pos.getZ() + 0.5D;
            int yPos = this.pos.getY();

            if (this.adjacentChestZPos != null) {
                d2 += 0.5D;
            }

            if (this.adjacentChestXPos != null) {
                d0 += 0.5D;
            }

            this.world.playSound(null, d0, (double) yPos + 0.5D, d2, SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
            this.lidAngle = 0.0F;
        }
    }
}
