package me.elephant1214.paperfixes.configuration;

import com.google.gson.*;
import me.elephant1214.paperfixes.PaperFixes;
import net.minecraft.launchwrapper.Launch;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.MixinEnvironment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public final class PaperFixesConfig {
    // Features
    public boolean enableSpawnChunkGamerule = true;
    public int defaultSpawnChunkRadius = 3;
    public TickLoopMode enhancedTickLoopMode = TickLoopMode.DYNAMIC_SLEEP_TIME;

    // Performance
    public boolean allowIoThreadSleep = false;
    public boolean cacheBlockDensities = true;
    public boolean cacheLastChunk = true;
    public boolean clientCacheLastChunk = true;
    public boolean dontHandleChestAnimInTick = true;
    public boolean removeSquidSetSeedCalls = true;
    public boolean reduceRegionIoOps = true;
    public boolean optimizedEntityDataMap = true;
    public boolean queueChunkSaving = true;
    public boolean trimRegionCache = true;
    public boolean useSharedRandomForEntities = true;

    // Bug fixes
    public boolean clearPacketQueue = true;
    public boolean fixMc54738 = true;
    public boolean fixMc80966 = true;
    public boolean fixMc133373 = true;
    public boolean fixMc98153 = true;
    public boolean fixWaterMobSpawnCheck = true;
    public boolean preventHangingTeCrashes = true;
    public boolean removeInvalidMobSpawners = true;
    public boolean removeNullTileEntities = false;
    public boolean sortEnchantments = true;
    public boolean stopDeadEntityProcessing = true;

    public static final PaperFixesConfig INSTANCE = new PaperFixesConfig();
    private final File CONFIG_FILE = new File(Launch.minecraftHome, "config" + File.separator + "paperfixes.json");

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private PaperFixesConfig() {
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try {
            if (!CONFIG_FILE.exists()) {
                CONFIG_FILE.createNewFile();
                writeConfig(gson);
            } else {
                BufferedReader fileReader = Files.newBufferedReader(CONFIG_FILE.toPath());
                JsonObject json = gson.fromJson(fileReader, JsonObject.class);
                ConfigReader reader = new ConfigReader(json);
                deserialize(reader);
                fileReader.close();
            }
        } catch (Exception e) {
            PaperFixes.LOGGER.error("Could not read config: ", e);
            writeConfig(gson);
        }
    }

    private void writeConfig(@NotNull Gson gson) {
        try {
            final BufferedWriter writer = Files.newBufferedWriter(CONFIG_FILE.toPath());
            gson.toJson(this.serialize(), JsonObject.class, writer);
            writer.close();
        } catch (IOException e) {
            PaperFixes.LOGGER.error("Could not write config: ", e);
        }
    }

    private JsonObject serialize() {
        final ConfigWriter config = new ConfigWriter();

        config.getRoot().addProperty("guide", "See https://github.com/Elephant1214/PaperFixes/blob/main/DETAILS.md for information about each option.");

        // Features
        config.addFeature(ENABLE_SPAWN_CHUNK_GAMERULE, new JsonPrimitive(enableSpawnChunkGamerule));
        config.addFeature(DEFAULT_SPAWN_CHUNK_RADIUS, new JsonPrimitive(defaultSpawnChunkRadius));
        config.addFeature(ENHANCED_TICK_LOOP_MODE, new JsonPrimitive(enhancedTickLoopMode.toString()));

        // Performance
        config.addPerformance(ALLOW_IO_THREAD_SLEEP, allowIoThreadSleep);
        config.addPerformance(CACHE_BLOCK_DENSITIES, cacheBlockDensities);
        config.addPerformance(CACHE_LAST_CHUNK, cacheLastChunk);
        if (isClient()) {
            config.addPerformance(CLIENT_CACHE_LAST_CHUNK, clientCacheLastChunk);
        }
        if (!isClient()) {
            config.addPerformance(DONT_HANDLE_CHEST_ANIM_IN_TICK, dontHandleChestAnimInTick);
        }
        config.addPerformance(OPTIMIZED_ENTITY_DATA_MAP, optimizedEntityDataMap);
        config.addPerformance(QUEUE_CHUNK_SAVING, queueChunkSaving);
        config.addPerformance(REDUCE_REGION_IO_OPS, reduceRegionIoOps);
        config.addPerformance(REMOVE_SQUID_SET_SEED_CALLS, removeSquidSetSeedCalls);
        config.addPerformance(TRIM_REGION_CACHE, trimRegionCache);
        config.addPerformance(USE_SHARED_RANDOM_FOR_ENTITIES, useSharedRandomForEntities);

        // Bug fixes
        config.addBugfix(CLEAR_PACKET_QUEUE, clearPacketQueue);
        config.addBugfix(FIX_MC_54738, fixMc54738);
        config.addBugfix(FIX_MC_80966, fixMc80966);
        config.addBugfix(FIX_MC_98153, fixMc98153);
        config.addBugfix(FIX_MC_133373, fixMc133373);
        config.addBugfix(FIX_WATER_MOB_SPAWN_CHECK, fixWaterMobSpawnCheck);
        config.addBugfix(PREVENT_HANGING_TE_CRASHES, preventHangingTeCrashes);
        config.addBugfix(REMOVE_INVALID_MOB_SPAWNERS, removeInvalidMobSpawners);
        config.addBugfix(REMOVE_NULL_TILE_ENTITIES, removeNullTileEntities);
        config.addBugfix(SORT_ENCHANTMENTS, sortEnchantments);
        config.addBugfix(STOP_DEAD_ENTITY_PROCESS, stopDeadEntityProcessing);

        return config.getRoot();
    }

    private void deserializeTickLoopMode(@NotNull ConfigReader reader) {
        String mode = reader.getFeature(ENHANCED_TICK_LOOP_MODE).getAsString();
        try {
            TickLoopMode.valueOf(mode);
        } catch (IllegalArgumentException e) {
            PaperFixes.LOGGER.error("Invalid tick loop mode '{}', please fix your config. Falling back to {}.", mode, TickLoopMode.DYNAMIC_SLEEP_TIME.toString());
        }
    }

    private void deserialize(@NotNull ConfigReader reader) {
        // Features
        enableSpawnChunkGamerule = reader.getFeature(ENABLE_SPAWN_CHUNK_GAMERULE).getAsBoolean();
        defaultSpawnChunkRadius = reader.getFeature(DEFAULT_SPAWN_CHUNK_RADIUS).getAsInt();
        deserializeTickLoopMode(reader);

        // Performance
        allowIoThreadSleep = reader.getPerformance(ALLOW_IO_THREAD_SLEEP);
        cacheBlockDensities = reader.getPerformance(CACHE_BLOCK_DENSITIES);
        cacheLastChunk = reader.getPerformance(CACHE_LAST_CHUNK);
        if (isClient()) {
            clientCacheLastChunk = reader.getPerformance(CLIENT_CACHE_LAST_CHUNK);
        }
        if (!isClient()) {
            dontHandleChestAnimInTick = reader.getPerformance(DONT_HANDLE_CHEST_ANIM_IN_TICK);
        }
        optimizedEntityDataMap = reader.getPerformance(OPTIMIZED_ENTITY_DATA_MAP);
        queueChunkSaving = reader.getPerformance(QUEUE_CHUNK_SAVING);
        reduceRegionIoOps = reader.getPerformance(REDUCE_REGION_IO_OPS);
        removeSquidSetSeedCalls = reader.getPerformance(REMOVE_SQUID_SET_SEED_CALLS);
        trimRegionCache = reader.getPerformance(TRIM_REGION_CACHE);
        useSharedRandomForEntities = reader.getPerformance(USE_SHARED_RANDOM_FOR_ENTITIES);

        // Bug fixes
        clearPacketQueue = reader.getBugfix(CLEAR_PACKET_QUEUE);
        fixMc54738 = reader.getBugfix(FIX_MC_54738);
        fixMc80966 = reader.getBugfix(FIX_MC_80966);
        fixMc98153 = reader.getBugfix(FIX_MC_98153);
        fixMc133373 = reader.getBugfix(FIX_MC_133373);
        fixWaterMobSpawnCheck = reader.getBugfix(FIX_WATER_MOB_SPAWN_CHECK);
        preventHangingTeCrashes = reader.getBugfix(PREVENT_HANGING_TE_CRASHES);
        removeInvalidMobSpawners = reader.getBugfix(REMOVE_INVALID_MOB_SPAWNERS);
        removeNullTileEntities = reader.getBugfix(REMOVE_NULL_TILE_ENTITIES);
        sortEnchantments = reader.getBugfix(SORT_ENCHANTMENTS);
        stopDeadEntityProcessing = reader.getBugfix(STOP_DEAD_ENTITY_PROCESS);
    }

    private static boolean isClient() {
        return MixinEnvironment.getCurrentEnvironment().getSide() == MixinEnvironment.Side.CLIENT;
    }

    // Features
    private static final String ENHANCED_TICK_LOOP_MODE = "enhancedTickLoopMode";
    private static final String ENABLE_SPAWN_CHUNK_GAMERULE = "enableSpawnChunkGamerule";
    private static final String DEFAULT_SPAWN_CHUNK_RADIUS = "defaultSpawnChunkRadius";

    // Performance
    private static final String ALLOW_IO_THREAD_SLEEP = "allowIoThreadSleep";
    private static final String CACHE_BLOCK_DENSITIES = "cacheBlockDensities";
    private static final String CACHE_LAST_CHUNK = "cacheLastChunk";
    private static final String CLIENT_CACHE_LAST_CHUNK = "clientCacheLastChunk";
    private static final String DONT_HANDLE_CHEST_ANIM_IN_TICK = "dontHandleChestAnimInTick";
    private static final String OPTIMIZED_ENTITY_DATA_MAP = "optimizedEntityDataMap";
    private static final String QUEUE_CHUNK_SAVING = "queueChunkSaving";
    private static final String REDUCE_REGION_IO_OPS = "reduceRegionIoOps";
    private static final String REMOVE_SQUID_SET_SEED_CALLS = "removeSquidSetSeedCalls";
    private static final String TRIM_REGION_CACHE = "trimRegionCache";
    private static final String USE_SHARED_RANDOM_FOR_ENTITIES = "useSharedRandomForEntities";

    // Bug fixes
    private static final String CLEAR_PACKET_QUEUE = "clearPacketQueue";
    private static final String FIX_MC_54738 = "fixMc54738";
    private static final String FIX_MC_80966 = "fixMc80966";
    private static final String FIX_MC_98153 = "fixMc98153";
    private static final String FIX_MC_133373 = "fixMc133373";
    private static final String FIX_WATER_MOB_SPAWN_CHECK = "fixWaterMobSpawnCheck";
    private static final String PREVENT_HANGING_TE_CRASHES = "preventHangingTileEntityCrashes";
    private static final String REMOVE_INVALID_MOB_SPAWNERS = "removeInvalidMobSpawners";
    private static final String REMOVE_NULL_TILE_ENTITIES = "removeNullTileEntities";
    private static final String SORT_ENCHANTMENTS = "sortEnchantments";
    private static final String STOP_DEAD_ENTITY_PROCESS = "stopDeadEntityProcessing";
}
