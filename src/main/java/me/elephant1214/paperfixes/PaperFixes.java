package me.elephant1214.paperfixes;

import me.elephant1214.paperfixes.command.CommandTPS;
import me.elephant1214.paperfixes.configuration.PaperFixesConfig;
import me.elephant1214.paperfixes.manager.ExplosionDensityCacheManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(name = PaperFixes.NAME, modid = PaperFixes.MOD_ID, version = PaperFixes.VERSION, acceptedMinecraftVersions = "1.12.2", acceptableRemoteVersions = "*")
public class PaperFixes {
    public static final String NAME = "PaperFixes";
    public static final String MOD_ID = "paperfixes";
    public static final String VERSION = "0.4.2-BETA";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static ExplosionDensityCacheManager explosionDensityCache = null;

    @Mod.EventHandler
    public void serverStartingEvent(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandTPS());
    }

    public PaperFixes() {
        if (PaperFixesConfig.cacheBlockDensities) {
            explosionDensityCache = new ExplosionDensityCacheManager();
        }
    }
}