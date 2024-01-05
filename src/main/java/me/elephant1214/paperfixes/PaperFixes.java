package me.elephant1214.paperfixes;

import io.papermc.paper.CacheKey;
import me.elephant1214.paperfixes.command.CommandTPS;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

@Mod(name = PaperFixes.NAME, modid = PaperFixes.MOD_ID, version = PaperFixes.VERSION, acceptedMinecraftVersions = "1.12.2", acceptableRemoteVersions = "*")
public class PaperFixes {
    public static final String NAME = "PaperFixes";
    public static final String MOD_ID = "paperfixes";
    public static final String VERSION = "0.1.2";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static final HashMap<CacheKey, Float> explosionDensityCache = new HashMap<>();

    @Mod.EventHandler
    public void serverStartingEvent(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandTPS());
    }

    public PaperFixes() {
    }
}