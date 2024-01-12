package me.elephant1214.paperfixes.configuration;

import me.elephant1214.paperfixes.PaperFixes;
import net.minecraftforge.common.config.Config;

@Config(modid = PaperFixes.MOD_ID)
public class PaperFixesConfig {
    @Config.RequiresMcRestart
    @Config.Name("keepSpawnLoaded")
    @Config.Comment({
            "Whether spawn chunks should always be loaded.",
            "Turn this off if you don't have things like mob farms at spawn so that you aren't wasting resources.",
            "See the README on GitHub for a more in-depth explanation."
    })
    public static boolean keepSpawnInMemory = true;

    @Config.RequiresMcRestart
    @Config.Comment(
            "Whether block densities should be cached to make explosion calculations faster."
    )
    public static boolean cacheBlockDensities = true;

    @Config.Name("Tick Loop")
    public static TickLoopConfig tickLoop = new TickLoopConfig();

    public static class TickLoopConfig {
        @Config.RequiresMcRestart
        @Config.Comment({"Whether the tick loop should be stopped from sleeping when you receive a \"Can't keep up! Is the server overloaded?\" message " +
                "and instead tick as quickly as possible to get back to 20 TPS.",
                "Incompatible with keepTpsAtOrAbove19."
        })
        public boolean noTickLoopSleepOnLag = false;

        @Config.RequiresMcRestart
        @Config.Comment({
                "Whether the tick loop should be stopped from sleeping when TPS drops below 19 and instead tick as quickly as possible to try to get back to 20.",
                "Incompatible with noTickLoopSleepOnLag."
        })
        public boolean keepTpsAtOrAbove19 = true;
    }
}
