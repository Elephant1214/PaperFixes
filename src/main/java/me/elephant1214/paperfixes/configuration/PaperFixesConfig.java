package me.elephant1214.paperfixes.configuration;

import me.elephant1214.paperfixes.PaperFixes;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RequiresMcRestart;

@Config(modid = PaperFixes.MOD_ID)
public class PaperFixesConfig {
    @RequiresMcRestart
    @Name("Keep Spawn Loaded")
    @Comment({
            "Whether spawn chunks should always be loaded.",
            "Turn this off if you don't have things like mob farms at spawn so that you aren't wasting resources.",
            "See the README on GitHub for a more in-depth explanation."
    })
    public static boolean keepSpawnLoaded = false;

    @RequiresMcRestart
    @Name("Cache Block Densities")
    @Comment("Whether block densities should be cached to make explosion calculations faster.")
    public static boolean cacheBlockDensities = true;

    @RequiresMcRestart
    @Name("Enhanced Tick Loop")
    @Comment({
            "Enhanced tick loop mode.",
            "DYNAMIC_SLEEP_TIME: Adjusts sleep time dynamically to try to stay as close to 20 TPS as possible.",
            "KEEP_TPS_AT_OR_ABOVE_19: Stops sleeping entirely to get back up to 19 TPS as soon as it drops below."
    })
    public static TickLoopMode enhancedTickLoopMode = TickLoopMode.DYNAMIC_SLEEP_TIME;
    
    @RequiresMcRestart
    @Name("Bug Fixes")
    @Comment("Toggles for all bug fixes.")
    public static BugFixesSection bugFixes = new BugFixesSection();
    
    public static class BugFixesSection {
        @Comment("MC-54738 fix")
        public boolean mc54738Fix = true;

        @Comment("MC-80966 fix")
        public boolean mc80966Fix = true;

        @Comment("MC-133373 fix")
        public boolean mc133373Fix = true;

        @Comment("MC-98153 fix")
        public boolean mc98153Fix = true;

        @Comment("Check for and remove invalid mob spawners (now with 100% less bugs!)")
        public boolean removeInvalidMobSpawners = true;

        @Comment("Fixes crashes caused by hanging tile entities.")
        public boolean preventHangingTileEntityCrashes = true;

        @Comment("Fixes issues with two items sometimes being seen as different because of the order enchantments are listed in the NBT data by sorting them.")
        public boolean sortEnchantments = true;
        
        @Comment("Fixes getCanSpawnHere in EntityWaterMob which just returns true. This adds an actual bounding box check, should fix issues with all water mobs spawning and dying.")
        public boolean fixCanSpawnHereCheck = true;
    }

    @RequiresMcRestart
    @Name("Performance Fixes")
    @Comment("Toggles for all performance fixes.")
    public static PerformanceFixesSection performanceFixes = new PerformanceFixesSection();

    public static class PerformanceFixesSection {
        @Comment("Stops explosions processing dead entities.")
        public boolean dontProcessDeadEntities = true;

        @Comment("Clears the packet queue for disconnecting players.")
        public boolean clearPacketQueueOnDisconnect = true;

        @Comment("Removes the chest animation and sound from the tick method.")
        public boolean removeChestAnimationsFromTick = true;

        @Comment("Removes fix for null tile entities when detected.")
        public boolean removeNullTileEntities = true;
        
        @Comment("Use a shared instance of Random for entities.")
        public boolean sharedRandomInstanceForEntities = true;

        @Comment("Reduces IO operations for loading region files.")
        public boolean reduceIoOpsForRegions = true;

        @Comment("Trim region cache upon reaching 256 loaded instead of clearing it entirely.")
        public boolean trimRegionCacheInsteadOfClearing = true;

        @Comment("Uses fastutil's Int2ObjectOpenHashMap for the data manager, significantly faster and with a smaller footprint.")
        public boolean optimizedEntityDataManagerHashMap = true;

        @Comment("Caches the last requested chunk to save map searches when the calls get for the same chunk multiple times in a row.")
        public boolean cacheLastChunk = true;
        
        @Comment("Switches to an actual queue for handling chunk saving instead of tossing iterators everywhere.")
        public boolean useQueueForChunkSaving = true;

        @Comment("Enables sleeping between chunk saves. This can cause memory issues if enabled.")
        public boolean enableIoThreadSleep = false;
    }
}
