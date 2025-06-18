package me.elephant1214.paperfixes.configuration;

import com.cleanroommc.configanytime.ConfigAnytime;
import me.elephant1214.paperfixes.PaperFixes;
import net.minecraftforge.common.config.Config;
import org.spongepowered.asm.mixin.MixinEnvironment;

import static me.elephant1214.paperfixes.util.TickConstants.NANOS_PER_MILLI;

@Config(modid = PaperFixes.MOD_ID)
public final class PaperFixesConfig {
    public static Bugfixes bugfixes = new Bugfixes();
    public static Features features = new Features();
    public static Performance performance = new Performance();

    @Config.RequiresMcRestart
    public static class Bugfixes {
        public boolean avoidItemMergeForFullStacks = true;
        public boolean clearPacketQueue = true;
        public boolean fixMc54738 = true;
        public boolean fixMc80966 = true;
        public boolean fixMc133373 = true;
        public boolean fixMc98153 = true;
        public boolean fixShulkerDupe = true;
        public boolean fixWaterMobSpawnCheck = true;
        public boolean dontOffloadBeaconColorUpdate = true;
        public boolean handleNullTileCrashes = true;
        public boolean removeInvalidMobSpawners = true;
        public boolean sortEnchantments = true;
        public boolean explosionsIgnoreDeadEntities = true;
    }

    @Config.RequiresMcRestart
    public static class Features {
        public boolean enableSpawnChunkGamerule = true;
        @Config.Comment("Only applies to new worlds and worlds that have not previously run with PaperFixes 2.0.0-beta.1 or newer")
        @Config.RangeInt(min = 0, max = 32)
        public int defaultSpawnChunkRadius = 3;
        public boolean useImprovedTickLoop = true;
        @Config.RangeInt(min = 0, max = (int) NANOS_PER_MILLI)
        public int tickLoopSpinTime = 200000;
        @Config.Comment("Requires the improved took loop to be enabled. This might be changed down the road.")
        public boolean runScheduledTasksDuringSleep = true;
        @Config.Comment("Only applies to dedicated servers. Border movement animations break when applying this on a client.")
        public boolean fastWorldBorder = true;
        @Config.Comment("If you do not care about world border movement animations and want PaperFixes to cache the world border on the client and integrated server anyway, use this option.")
        public boolean clientFastWorldBorder = false;

        public boolean useFastBorder() {
            return this.fastWorldBorder && (MixinEnvironment.getCurrentEnvironment().getSide() != MixinEnvironment.Side.CLIENT || this.clientFastWorldBorder);
        }
    }

    @Config.RequiresMcRestart
    public static class Performance {
        public boolean allowIoThreadSleep = false;
        public boolean cacheBlockDensities = true;
        public boolean cacheLastChunk = true;
        public boolean clientCacheLastChunk = true;
        public boolean fastChests = true;
        public boolean smartRegionRead = true;
        public boolean fastEntityDataMap = true;
        public boolean optimizePathfinding = true;
        public boolean optimizedTaskQueue = true;
        public boolean queueChunkSaving = true;
        public boolean trimRegionCache = true;
        public boolean useSharedRandomForEntities = true;
    }

    static {
        try {
            Class.forName("com.cleanroommc.configanytime.ConfigAnytime", false, PaperFixesConfig.class.getClassLoader());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(
                    "PaperFixes requires ConfigAnytime, but it is not currently present on the classpath. Please add ConfigAnytime 3.0 or newer and try starting your game again." +
                            "\nIf this issue persists after adding ConfigAnytime and you are using the latest version, please open an issue at https://github.com/Elephant1214/PaperFixes."
            );
        }

        ConfigAnytime.register(PaperFixesConfig.class);
    }
}
