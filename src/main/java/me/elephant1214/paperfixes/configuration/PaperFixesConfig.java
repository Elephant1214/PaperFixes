package me.elephant1214.paperfixes.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import me.elephant1214.paperfixes.PaperFixes;
import net.minecraft.launchwrapper.Launch;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

public final class PaperFixesConfig {
    public TickLoopMode enhancedTickLoopMode = TickLoopMode.DYNAMIC_SLEEP_TIME;
    public boolean keepSpawnLoaded = false;
    public boolean cacheBlockDensities = true;
    public boolean mc54738Fix = true;
    public boolean mc80966Fix = true;
    public boolean mc133373Fix = true;
    public boolean mc98153Fix = true;
    public boolean removeInvalidMobSpawners = true;
    public boolean preventHangingTileEntityCrashes = true;
    public boolean sortEnchantments = true;
    public boolean fixCanSpawnHereCheck = true;
    public boolean dontProcessDeadEntities = true;
    public boolean clearPacketQueueOnDisconnect = true;
    public boolean removeChestAnimationsFromTick = true;
    public boolean removeNullTileEntities = false;
    public boolean sharedRandomInstanceForEntities = true;
    public boolean removeSquidSetSeedCalls = true;
    public boolean reduceIoOpsForRegions = true;
    public boolean trimRegionCacheInsteadOfClearing = true;
    public boolean optimizedEntityDataManagerHashMap = true;
    public boolean cacheLastChunk = true;
    public boolean useQueueForChunkSaving = true;
    public boolean enableIoThreadSleep = false;
    public boolean clientCacheLastChunk = true;

    public static final String ENABLED = "enabled";
    private static final File FILE = new File(Launch.minecraftHome, "config" + File.separator + "paperfixes.json");
    public static final PaperFixesConfig INSTANCE = new PaperFixesConfig();

    private PaperFixesConfig() {
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();

        if (!FILE.exists()) {
            writeConfig(gson);
        } else {
            try {
                this.deserialize(gson.fromJson(Files.newBufferedReader(FILE.toPath()), JsonObject.class));
            } catch (Exception e) {
                PaperFixes.LOGGER.error("Could not read config: ", e);
                writeConfig(gson);
            }
        }
    }

    private void writeConfig(Gson gson) {
        try {
            final BufferedWriter writer = Files.newBufferedWriter(FILE.toPath());
            gson.toJson(this.serialize(), JsonObject.class, writer);
            writer.close();
        } catch (IOException e) {
            PaperFixes.LOGGER.error("Could not write config: ", e);
        }
    }
    
    public JsonObject serialize() {
        final JsonObject root = new JsonObject();
        
        addTickLoopConfigOption(root, enhancedTickLoopMode.toString());
        addConfigOption(root, "keepSpawnLoaded", "Whether spawn chunks should always be loaded.", keepSpawnLoaded);
        addConfigOption(root, "cacheBlockDensities", "Caches block densities to make explosion calculations faster", cacheBlockDensities);
        addConfigOption(root, "mc54738Fix", "Fixes MC-54738", mc54738Fix);
        addConfigOption(root, "mc80966Fix", "Fixes MC-80966", mc80966Fix);
        addConfigOption(root, "mc133373Fix", "Fixes MC-133373", mc133373Fix);
        addConfigOption(root, "mc98153Fix", "Fixes MC-98153", mc98153Fix);
        addConfigOption(root, "removeInvalidMobSpawners", "Removes invalid mob spawners when detected.", removeInvalidMobSpawners);
        addConfigOption(root, "preventHangingTileEntityCrashes", "Fixes crashes caused by hanging tile entities.", preventHangingTileEntityCrashes);
        addConfigOption(root, "sortEnchantments", "Fixes issues with two items sometimes being seen as different because of the order enchantments are listed in the NBT data by sorting them.", sortEnchantments);
        addConfigOption(root, "fixCanSpawnHereCheck", "Fixes getCanSpawnHere in EntityWaterMob which just returns true. This adds an actual bounding box check, should fix issues with all water mobs spawning and dying.", fixCanSpawnHereCheck);
        addConfigOption(root, "dontProcessDeadEntities", "Stops explosions processing dead entities.", dontProcessDeadEntities);
        addConfigOption(root, "clearPacketQueueOnDisconnect", "Clears the packet queue for disconnecting players.", clearPacketQueueOnDisconnect);
        addConfigOption(root, "removeChestAnimationsFromTick", "Removes the chest animation and sound from the tick method.", removeChestAnimationsFromTick);
        addConfigOption(root, "removeNullTileEntities", "Removes null tile entities when theyre detected. Can cause problems with some mods, check GitHub for any KNOWN issues before you enable this.", removeNullTileEntities);
        addConfigOption(root, "sharedRandomInstanceForEntities", "Use a shared instance of Random for entities. Use removeSquidSetSeedCalls with this or you will get setSeed access errors logged.", sharedRandomInstanceForEntities);
        addConfigOption(root, "removeSquidSetSeedCalls", "Stops pointless Random.setSeed calls made by the squid class.", removeSquidSetSeedCalls);
        addConfigOption(root, "reduceIoOpsForRegions", "Reduces IO operations for loading region files.", reduceIoOpsForRegions);
        addConfigOption(root, "trimRegionCacheInsteadOfClearing", "Trim region cache upon reaching 256 loaded instead of clearing it entirely.", trimRegionCacheInsteadOfClearing);
        addConfigOption(root, "optimizedEntityDataManagerHashMap", "Uses fastutil Int2ObjectOpenHashMap for the data manager, significantly faster and with a smaller footprint.", optimizedEntityDataManagerHashMap);
        addConfigOption(root, "cacheLastChunk", "Caches the last requested chunk to save map searches when the calls get for the same chunk multiple times in a row.", cacheLastChunk);
        addConfigOption(root, "useQueueForChunkSaving", "Switches to an actual queue for handling chunk saving instead of tossing iterators everywhere.", useQueueForChunkSaving);
        addConfigOption(root, "enableIoThreadSleep", "Enables sleeping between chunk saves. This can cause memory issues if enabled.", enableIoThreadSleep);
        addConfigOption(root, "clientCacheLastChunk", "Does the same as cacheLastChunk, but for the client chunk map.", clientCacheLastChunk);
        
        return root;
    }

    private static void addTickLoopConfigOption(JsonObject root, String value) {
        final JsonObject entry = new JsonObject();
        entry.addProperty("description", "The took loop mode to use. Dynamic sleep time sleeps dynamically to avoid sleeping too much. Keep TPS at or above 19 keeps TPS as close to 19 as possible when lag occurs.");
        entry.addProperty("modes", "DYNAMIC_SLEEP_TIME, KEEP_TPS_AT_OR_ABOVE_19, OFF");
        entry.addProperty("mode", value);
        root.add("enhancedTickLoopMode", entry);
    }
    
    private static void addConfigOption(JsonObject root, String name, String description, boolean value) {
        final JsonObject entry = new JsonObject();
        entry.addProperty("description", description);
        entry.addProperty(ENABLED, value);
        root.add(name, entry);
    }
    
    public void deserialize(JsonObject data) {
        Objects.requireNonNull(data);
        
        enhancedTickLoopMode = TickLoopMode.valueOf(data.get("enhancedTickLoopMode").getAsJsonObject().get("mode").getAsString());
        keepSpawnLoaded = data.get("keepSpawnLoaded").getAsJsonObject().get(ENABLED).getAsBoolean();
        cacheBlockDensities = data.get("cacheBlockDensities").getAsJsonObject().get(ENABLED).getAsBoolean();
        mc54738Fix = data.get("mc54738Fix").getAsJsonObject().get(ENABLED).getAsBoolean();
        mc80966Fix = data.get("mc80966Fix").getAsJsonObject().get(ENABLED).getAsBoolean();
        mc133373Fix = data.get("mc133373Fix").getAsJsonObject().get(ENABLED).getAsBoolean();
        mc98153Fix = data.get("mc98153Fix").getAsJsonObject().get(ENABLED).getAsBoolean();
        removeInvalidMobSpawners = data.get("removeInvalidMobSpawners").getAsJsonObject().get(ENABLED).getAsBoolean();
        preventHangingTileEntityCrashes = data.get("preventHangingTileEntityCrashes").getAsJsonObject().get(ENABLED).getAsBoolean();
        sortEnchantments = data.get("sortEnchantments").getAsJsonObject().get(ENABLED).getAsBoolean();
        fixCanSpawnHereCheck = data.get("fixCanSpawnHereCheck").getAsJsonObject().get(ENABLED).getAsBoolean();
        dontProcessDeadEntities = data.get("dontProcessDeadEntities").getAsJsonObject().get(ENABLED).getAsBoolean();
        clearPacketQueueOnDisconnect = data.get("clearPacketQueueOnDisconnect").getAsJsonObject().get(ENABLED).getAsBoolean();
        removeChestAnimationsFromTick = data.get("removeChestAnimationsFromTick").getAsJsonObject().get(ENABLED).getAsBoolean();
        removeNullTileEntities = data.get("removeNullTileEntities").getAsJsonObject().get(ENABLED).getAsBoolean();
        sharedRandomInstanceForEntities = data.get("sharedRandomInstanceForEntities").getAsJsonObject().get(ENABLED).getAsBoolean();
        removeSquidSetSeedCalls = data.get("removeSquidSetSeedCalls").getAsJsonObject().get(ENABLED).getAsBoolean();
        reduceIoOpsForRegions = data.get("reduceIoOpsForRegions").getAsJsonObject().get(ENABLED).getAsBoolean();
        trimRegionCacheInsteadOfClearing = data.get("trimRegionCacheInsteadOfClearing").getAsJsonObject().get(ENABLED).getAsBoolean();
        optimizedEntityDataManagerHashMap = data.get("optimizedEntityDataManagerHashMap").getAsJsonObject().get(ENABLED).getAsBoolean();
        cacheLastChunk = data.get("cacheLastChunk").getAsJsonObject().get(ENABLED).getAsBoolean();
        useQueueForChunkSaving = data.get("useQueueForChunkSaving").getAsJsonObject().get(ENABLED).getAsBoolean();
        enableIoThreadSleep = data.get("enableIoThreadSleep").getAsJsonObject().get(ENABLED).getAsBoolean();
        clientCacheLastChunk = data.get("clientCacheLastChunk").getAsJsonObject().get(ENABLED).getAsBoolean();
    }
}
