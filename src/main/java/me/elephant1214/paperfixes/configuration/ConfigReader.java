package me.elephant1214.paperfixes.configuration;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

public class ConfigReader {
    private final JsonObject root;

    ConfigReader(@NotNull JsonObject root) {
        this.root = root;
    }

    JsonElement getFeature(@NotNull String name) {
        return root.getAsJsonObject("features").get(name);
    }

    boolean getPerformance(@NotNull String name) {
        return root.getAsJsonObject("performance").get(name).getAsBoolean();
    }

    boolean getBugfix(@NotNull String name) {
        return root.getAsJsonObject("bugfixes").get(name).getAsBoolean();
    }
}
