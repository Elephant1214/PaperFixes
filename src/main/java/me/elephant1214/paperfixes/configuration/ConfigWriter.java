package me.elephant1214.paperfixes.configuration;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

final class ConfigWriter {
    private final JsonObject root = new JsonObject();
    private final JsonObject features = new JsonObject();
    private final JsonObject performance = new JsonObject();
    private final JsonObject bugfixes = new JsonObject();

    ConfigWriter() {
    }

    void addFeature(@NotNull String name, @NotNull JsonElement value) {
        if (!root.has("features")) {
            root.add("features", features);
        }
        features.add(name, value);
    }

    void addPerformance(@NotNull String name, boolean value) {
        if (!root.has("performance")) {
            root.add("performance", performance);
        }
        performance.addProperty(name, value);
    }

    void addBugfix(@NotNull String name, boolean value) {
        if (!root.has("bugfixes")) {
            root.add("bugfixes", bugfixes);
        }
        bugfixes.addProperty(name, value);
    }

    @NotNull JsonObject getRoot() {
        return root;
    }
}
