package me.elephant1214.paperfixes.configuration;

import me.elephant1214.paperfixes.PaperFixes;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RequiresMcRestart;

@Config(modid = PaperFixes.MOD_ID)
public class PaperFixesConfig {
    @RequiresMcRestart
    @Name("keepSpawnLoaded")
    @Comment({
            "Whether spawn chunks should always be loaded.",
            "Turn this off if you don't have things like mob farms at spawn so that you aren't wasting resources.",
            "See the README on GitHub for a more in-depth explanation."
    })
    public static boolean keepSpawnInMemory = false;

    @RequiresMcRestart
    @Comment("Whether block densities should be cached to make explosion calculations faster.")
    public static boolean cacheBlockDensities = true;

    @RequiresMcRestart
    @Name("Enhanced Tick Loop")
    @Comment("Options for the enhanced tick loop.")
    public static TickLoopSection enhancedTickLoop = new TickLoopSection();

    public static class TickLoopSection {
        @Comment("Toggles the enhanced took loop. The below options do nothing if this is set to false.")
        public boolean enabled = true;

        @Comment({
                "Whether the tick loop should be stopped from sleeping when TPS drops below 19 and instead tick as quickly as possible to try to get back to 20.",
                "Incompatible with noTickLoopSleepOnLag."
        })
        public boolean keepTpsAtOrAbove19 = true;

        @Comment({
                "Whether the tick loop should be stopped from sleeping when you receive a \"Can't keep up! Is the server overloaded?\" message " +
                "and instead tick as quickly as possible to get back to 20 TPS.",
                "Incompatible with keepTpsAtOrAbove19."
        })
        public boolean noTickLoopSleepOnLag = false;
    }

    @RequiresMcRestart
    @Name("Mixins")
    @Comment("Toggles for all mixins in the mod. Can also be used to disable some features.")
    public static MixinsSection mixinsSection = new MixinsSection();

    public static class MixinsSection {
        @Comment("Toggles the mixin which removes invalid mob spawners")
        public boolean removeInvalidMobSpawners = true;

        @Comment("Toggles the mixin for the MC-54738 fix")
        public boolean mc54738Fix = true;

        @Comment({
                "Toggles the explosion mixin to disable the fix for explosions processing dead entities",
                "This will also break cacheBlockDensities, you must disable it if you disable this."
        })
        public boolean explosionMixin = true;

        @Comment("Toggles the mixin for the MC-80966 fix")
        public boolean mc80966Fix = true;

        @Comment("Toggles the mixin which clears the packet queue for a disconnecting player")
        public boolean clearPacketQueueOnDisconnect = true;

        @Comment("Toggles the mixin for the MC-133373 fix")
        public boolean mc133373Fix = true;

        @Comment("Toggles the mixin for the MC-98153 fix")
        public boolean mc98153Fix = true;

        @Comment("Toggles the mixin to remove the chest animations from the tick function")
        public boolean removeChestAnimationsFromTick = true;

        @Comment({
                "Toggles the world mixin to disable the fix for null tile entities.",
                "This will also stop keepSpawnInMemory from functioning and break it, effectively making it always enabled."
        })
        public boolean worldMixin = true;
    }
}
