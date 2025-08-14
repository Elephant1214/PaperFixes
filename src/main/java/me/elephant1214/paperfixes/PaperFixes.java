package me.elephant1214.paperfixes;

import me.elephant1214.paperfixes.configuration.PaperFixesConfig;
import me.elephant1214.paperfixes.gamerule.SpawnChunkRule;
import me.elephant1214.paperfixes.manager.FastWorldBorder;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
@Mod(
        name = PaperFixes.NAME,
        modid = PaperFixes.MOD_ID,
        version = PaperFixes.VERSION,
        acceptedMinecraftVersions = "1.12.2",
        acceptableRemoteVersions = "*",
        useMetadata = true
)
public final class PaperFixes {
    public static final String NAME = "PaperFixes";
    public static final String MOD_ID = "paperfixes";
    public static final String VERSION = "2.0.0";
    public static final Logger LOGGER = LogManager.getLogger(NAME);

    public PaperFixes() {
    }

    @Mod.EventHandler
    public void preInit(@NotNull FMLPreInitializationEvent event) {
        if (PaperFixesConfig.features.spawnChunkGamerule) {
            MinecraftForge.EVENT_BUS.register(new SpawnChunkRule());
        }

        if (PaperFixesConfig.enableFastBorder()) {
            MinecraftForge.EVENT_BUS.register(new FastWorldBorder.Events());
        }
    }

    private static MinecraftServer server = null;

    /**
     * Should be always considered null unless there is a server running.
     */
    public static MinecraftServer getServer() {
        return server;
    }

    @SideOnly(Side.CLIENT)
    public static boolean isOnServer() {
        final Minecraft minecraft = Minecraft.getMinecraft();
        return minecraft.isSingleplayer() || minecraft.getCurrentServerData() != null;
    }

    @Mod.EventHandler
    public void serverAboutToStart(@NotNull FMLServerAboutToStartEvent event) {
        server = event.getServer();
    }
}