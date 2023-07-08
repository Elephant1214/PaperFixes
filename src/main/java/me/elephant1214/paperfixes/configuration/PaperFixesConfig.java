package me.elephant1214.paperfixes.configuration;

import me.elephant1214.paperfixes.PaperFixes;
import net.minecraftforge.common.config.Config;

@Config(modid = PaperFixes.MOD_ID)
public class PaperFixesConfig {
    @Config.RequiresMcRestart
    @Config.Name("keepSpawnLoaded")
    @Config.Comment({
            "Whether spawn chunks should always be loaded.",
            "Turn this off if you don't have things like mob farms at spawn so that you aren't wasting resources. " +
                    "See the README on GitHub for a more in-depth explanation."
    })
    public static boolean keepSpawnInMemory = true;

    @Config.RequiresMcRestart
    @Config.Comment({
            "Whether block densities should be cached to make explosion calculations faster."
    })
    public static boolean cacheBlockDensities = true;
}
