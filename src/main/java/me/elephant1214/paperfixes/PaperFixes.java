package me.elephant1214.paperfixes;

import me.elephant1214.paperfixes.configuration.PaperFixesConfig;
import me.elephant1214.paperfixes.gamerule.SpawnChunkRule;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
@Mod(
        name = PaperFixes.NAME,
        modid = PaperFixes.MOD_ID,
        version = PaperFixes.VERSION,
        acceptedMinecraftVersions = "1.12.2",
        acceptableRemoteVersions = "*"
)
public final class PaperFixes {
    public static final String NAME = "PaperFixes";
    public static final String MOD_ID = "paperfixes";
    public static final String VERSION = "2.0.0-SNAPSHOT";
    public static final Logger LOGGER = LogManager.getLogger(NAME);

    public PaperFixes() {
    }

    @Mod.EventHandler
    public void preInit(@NotNull FMLPreInitializationEvent event) {
        if (PaperFixesConfig.INSTANCE.features.enableSpawnChunkGamerule) {
            MinecraftForge.EVENT_BUS.register(new SpawnChunkRule());
        }
    }
}