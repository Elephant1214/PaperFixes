package me.elephant1214.paperfixes.command;

import me.elephant1214.paperfixes.manager.TickManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import org.jetbrains.annotations.NotNull;

public class CommandTPS extends CommandBase {
    @Override
    public @NotNull String getName() {
        return "tps";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public @NotNull String getUsage(@NotNull ICommandSender sender) {
        return "/tps";
    }

    @Override
    public void execute(@NotNull MinecraftServer server, @NotNull ICommandSender sender, String @NotNull [] args) {
        final TextComponentString base = new TextComponentString("TPS from last 5s, 1m, 5m, 15m: ");
        final double[] tps = TickManager.INSTANCE.getTPS();
        base.appendSibling(formatTPS(tps[0]));
        for (int entry = 1; entry < 4; entry++) {
            base.appendSibling(new TextComponentString(", "));
            base.appendSibling(formatTPS(tps[entry]));
        }
        sender.sendMessage(base);
    }

    private static TextComponentString formatTPS(double tps) {
        final char colorChar = 167;
        return new TextComponentString(
                (tps > 18D ? colorChar + "a" : tps > 16D ? colorChar + "e" : colorChar + "c") + Math.round(tps * 100.0) / 100.0
        );
    }
}
