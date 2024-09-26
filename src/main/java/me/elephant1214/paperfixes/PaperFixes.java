package me.elephant1214.paperfixes;

import me.elephant1214.paperfixes.command.CommandTPS;
import me.elephant1214.paperfixes.configuration.PaperFixesConfig;
import me.elephant1214.paperfixes.configuration.TickLoopMode;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("unused")
@Mod(
        name = PaperFixes.NAME,
        modid = PaperFixes.MOD_ID,
        version = PaperFixes.VERSION,
        acceptedMinecraftVersions = "1.12.2",
        acceptableRemoteVersions = "*"
)
public class PaperFixes {
    public static final String NAME = "PaperFixes";
    public static final String MOD_ID = "paperfixes";
    public static final String VERSION = "1.3.0";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    @Mod.EventHandler
    public void serverStartingEvent(FMLServerStartingEvent event) {
        if (PaperFixesConfig.INSTANCE.enhancedTickLoopMode != TickLoopMode.OFF) {
            event.registerServerCommand(new CommandTPS());
        }
    }

    public PaperFixes() {
    }
}