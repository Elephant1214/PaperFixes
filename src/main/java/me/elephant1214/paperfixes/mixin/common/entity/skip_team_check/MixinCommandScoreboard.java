package me.elephant1214.paperfixes.mixin.common.entity.skip_team_check;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.command.CommandBase;
import net.minecraft.command.server.CommandScoreboard;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.stream.Collectors;

@Mixin(CommandScoreboard.class)
public abstract class MixinCommandScoreboard extends CommandBase {
    @ModifyExpressionValue(method = "joinTeam", at = @At(value = "INVOKE", target = "Lnet/minecraft/command/server/CommandScoreboard;getEntityList(Lnet/minecraft/server/MinecraftServer;Lnet/minecraft/command/ICommandSender;Ljava/lang/String;)Ljava/util/List;"))
    private List<Entity> removeNonPlayers(List<Entity> original) {
        return original.stream()
                .filter(entity -> entity instanceof EntityPlayer)
                .collect(Collectors.toList());
    }
}
