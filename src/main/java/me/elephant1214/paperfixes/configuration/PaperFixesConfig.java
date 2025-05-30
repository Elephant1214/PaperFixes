package me.elephant1214.paperfixes.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.elephant1214.paperfixes.PaperFixes;
import me.elephant1214.paperfixes.configuration.parts.Bugfixes;
import me.elephant1214.paperfixes.configuration.parts.Features;
import me.elephant1214.paperfixes.configuration.parts.Performance;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class PaperFixesConfig {
    final ConfigContent content;
    public final Bugfixes bugfixes;
    public final Features features;
    public final Performance performance;
    public static final PaperFixesConfig INSTANCE = new PaperFixesConfig();

    private final @NotNull Path CONFIG_FILE = new File(Launch.minecraftHome, "config" + File.separator + "paperfixes.json").toPath();

    private PaperFixesConfig() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        ConfigContent tempContent = new ConfigContent();
        try {
            if (Files.notExists(CONFIG_FILE)) {
                Files.createDirectories(CONFIG_FILE.getParent());
                Files.createFile(CONFIG_FILE);
                saveConfig(gson, tempContent);
            } else {
                tempContent = readConfig(gson);
            }
        } catch (Exception e) {
            PaperFixes.LOGGER.error("Could not create or read config", e);
        }
        this.content = tempContent;
        this.bugfixes = content.bugfixes;
        this.features = content.features;
        this.performance = content.performance;
    }


    private void saveConfig(Gson gson, ConfigContent content) {
        try (BufferedWriter writer = Files.newBufferedWriter(CONFIG_FILE)) {
            gson.toJson(content, writer);
        } catch (IOException e) {
            PaperFixes.LOGGER.error("Could not write to config", e);
        }
    }

    private ConfigContent resetConfig(Gson gson) {
        ConfigContent overwrite = new ConfigContent();
        saveConfig(gson, overwrite);
        return overwrite;
    }

    private ConfigContent readConfig(Gson gson) {
        try (BufferedReader reader = Files.newBufferedReader(CONFIG_FILE)) {
            JsonElement element = gson.fromJson(reader, JsonElement.class);
            if (element == null || element.isJsonNull()
                    || (element.isJsonObject() && element.getAsJsonObject().size() == 0)) {
                PaperFixes.LOGGER.warn("Config is empty, resetting it");
                return resetConfig(gson);
            }

            return deserializeAndCheck(gson, element.getAsJsonObject());
        } catch (IOException e) {
            PaperFixes.LOGGER.error("Could not read from config", e);
            return resetConfig(gson);
        }
    }

    private ConfigContent deserializeAndCheck(Gson gson, JsonObject object) {
        ConfigContent config = gson.fromJson(object, ConfigContent.class);

        int defaultSpawnChunkRadius = object.getAsJsonObject("features").get(DEFAULT_SPAWN_CHUNK_RADIUS).getAsInt();
        if (defaultSpawnChunkRadius < 0 || defaultSpawnChunkRadius > 32) {
            config.features.defaultSpawnChunkRadius = MathHelper.clamp(defaultSpawnChunkRadius, 0, 32);
        }

        saveConfig(gson, config); // Update either way to ensure no options are ever missing
        return config;
    }

    private static final String DEFAULT_SPAWN_CHUNK_RADIUS = "defaultSpawnChunkRadius";
}
