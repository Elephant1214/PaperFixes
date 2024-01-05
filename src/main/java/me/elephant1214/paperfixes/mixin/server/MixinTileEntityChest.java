package me.elephant1214.paperfixes.mixin.server;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntityChest.class)
public abstract class MixinTileEntityChest {
    @Shadow
    public abstract void checkForAdjacentChests();

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

    /**
     * @reason We never want this to run. I'm not even sure what would happen if it did other than problems.
     */
    @Inject(method = "update", at = @At("HEAD"), cancellable = true)
    private void noChestAnimationInTick(CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "openInventory", at = @At(value = "FIELD", target = "Lnet/minecraft/tileentity/TileEntityChest;world:Lnet/minecraft/world/World;"), allow = 1)
    private void handleOpenChest(EntityPlayer player, CallbackInfo ci) {
        BlockPos pos = ((TileEntityChest) (Object) this).getPos();
        World world = ((TileEntityChest) (Object) this).getWorld();
        this.checkForAdjacentChests();

        if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F && this.adjacentChestZNeg == null && this.adjacentChestXNeg == null) {
            this.lidAngle = 0.7F;

            double d0 = (double) pos.getZ() + 0.5D;
            double d1 = (double) pos.getX() + 0.5D;

            if (this.adjacentChestZPos != null) {
                d0 += 0.5D;
            }

            if (this.adjacentChestXPos != null) {
                d1 += 0.5D;
            }

            world.playSound(null, d1, (double) pos.getY() + 0.5D, d0, SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
        }
    }

    @Inject(method = "closeInventory", at = @At(value = "FIELD", target = "Lnet/minecraft/tileentity/TileEntityChest;world:Lnet/minecraft/world/World;"), allow = 1)
    private void handleCloseChest(EntityPlayer player, CallbackInfo ci) {
        BlockPos pos = ((TileEntityChest) (Object) this).getPos();
        World world = ((TileEntityChest) (Object) this).getWorld();

        if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F || this.numPlayersUsing > 0 && this.lidAngle < 1.0F) {
            float f = 0.1F;

            if (this.numPlayersUsing > 0) {
                this.lidAngle += f;
            } else {
                this.lidAngle -= f;
            }

            double d0 = (double) pos.getX() + 0.5D;
            double d2 = (double) pos.getZ() + 0.5D;
            int yPos = pos.getY();

            if (this.adjacentChestZPos != null) {
                d2 += 0.5D;
            }

            if (this.adjacentChestXPos != null) {
                d0 += 0.5D;
            }

            world.playSound(null, d0, (double) yPos + 0.5D, d2, SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
            this.lidAngle = 0.0F;
        }
    }
}
