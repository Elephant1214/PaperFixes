package me.elephant1214.paperfixes.mixin.common.entity.skip_team_check;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.Team;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class MixinEntity implements ICommandSender, ICapabilitySerializable<NBTTagCompound> {
    @Inject(method = "getTeam", at = @At("HEAD"), cancellable = true)
    private void dontCheckTeamsForNonPlayers(CallbackInfoReturnable<@Nullable Team> cir) {
        if (!(((Entity) (Object) this) instanceof EntityPlayer)) {
            cir.setReturnValue(null);
            cir.cancel();
        }
    }
}
