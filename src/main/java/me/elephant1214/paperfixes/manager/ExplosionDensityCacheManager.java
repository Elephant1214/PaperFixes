package me.elephant1214.paperfixes.manager;

import io.papermc.paper.CacheKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ExplosionDensityCacheManager {
    public static final ExplosionDensityCacheManager INSTANCE = new ExplosionDensityCacheManager();
    private final Map<@NotNull CacheKey, @Nullable Float> explosionDensityCache = new HashMap<>();

    public @Nullable Float getCached(@NotNull CacheKey key) {
        return this.explosionDensityCache.getOrDefault(key, null);
    }

    public void addCached(@NotNull CacheKey key, float density) {
        this.explosionDensityCache.put(key, density);
    }

    public void clearCache() {
        this.explosionDensityCache.clear();
    }
}
