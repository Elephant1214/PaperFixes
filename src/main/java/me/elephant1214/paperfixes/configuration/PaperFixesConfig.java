package me.elephant1214.paperfixes.configuration;

import com.cleanroommc.configanytime.ConfigAnytime;
import me.elephant1214.paperfixes.PaperFixes;
import net.minecraftforge.common.config.Config;
import org.spongepowered.asm.mixin.MixinEnvironment;

import static me.elephant1214.paperfixes.util.TickConstants.NANOS_PER_MILLI;

@Config(modid = PaperFixes.MOD_ID)
public final class PaperFixesConfig {
    @Config.RequiresMcRestart
    public static Bugfixes bugfixes = new Bugfixes();
    @Config.RequiresMcRestart
    public static Client client = new Client();
    @Config.RequiresMcRestart
    public static Features features = new Features();
    @Config.RequiresMcRestart
    public static Performance performance = new Performance();

    public static boolean enableFastBorder() {
        return features.fastWorldBorder && (MixinEnvironment.getCurrentEnvironment().getSide() != MixinEnvironment.Side.CLIENT || client.fastWorldBorder);
    }

    public static class Bugfixes {
        public boolean avoidItemMergeForFullStacks = true;
        public boolean clearPacketQueue = true;
        public boolean explosionsIgnoreDeadEntities = true;
        public boolean fixMc54738 = true;
        public boolean fixMc80966 = true;
        public boolean fixMc98153 = true;
        public boolean fixMc133373 = true;
        public boolean fixShulkerDispenseCrash = true;
        public boolean fixShulkerDupe = true;
        public boolean fixWaterMobSpawnCheck = true;
        public boolean dontOffloadBeaconColorUpdate = true;
        public boolean handleNullTileCrashes = true;
        public boolean removeInvalidMobSpawners = true;
        public boolean sortEnchantments = true;
    }

    public static class Client {
        public boolean cacheLastChunk = true;
        public boolean fastWorldBorder = false;
    }

    public static class Features {
        public boolean spawnChunkGamerule = true;
        @Config.RangeInt(min = 0, max = 32)
        public int spawnChunkRadius = 3;
        public boolean improvedTickLoop = true;
        @Config.RangeInt(min = 0, max = (int) NANOS_PER_MILLI)
        public int tickLoopSpinTime = 200000;
        public boolean runTasksDuringSleep = true;
        public boolean fastWorldBorder = true;
    }

    public static class Performance {
        public boolean ioThreadSleep = false;
        public boolean cacheBlockDensities = true;
        public boolean cacheLastChunk = true;
        public boolean compactLut = true;
        public boolean fastChests = true;
        public boolean smartRegionRead = true;
        public boolean fastEntityDataMap = true;
        public boolean optimizePathfinding = true;
        public boolean optimizedTaskQueue = true;
        public boolean pathingChunkCache = true;
        public boolean pathNodeCache = true;
        public boolean queueChunkSaving = true;
        public boolean trimRegionCache = true;
        public boolean sharedRandomForEntities = true;
    }

    static {
        try {
            Class.forName("com.cleanroommc.configanytime.ConfigAnytime", false, PaperFixesConfig.class.getClassLoader());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PaperFixes requires ConfigAnytime, but it is not currently present on the classpath. Please add ConfigAnytime 3.0 or newer and try starting your game again." + "\nIf this issue persists after adding ConfigAnytime and you are using the latest version of both mods, please open an issue at https://github.com/Elephant1214/PaperFixes.");
        }

        ConfigAnytime.register(PaperFixesConfig.class);
    }
}
