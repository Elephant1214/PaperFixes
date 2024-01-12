package me.elephant1214.paperfixes.manager;

import io.papermc.paper.CacheKey;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class ExplosionDensityCacheManager {
    private final Map<CacheKey, Float> explosionDensityCache = new HashMap<>();

    public @Nullable Float getCached(@Nonnull CacheKey key) {
        return this.explosionDensityCache.getOrDefault(key, null);
    }

    public void addCached(@Nonnull CacheKey key, float density) {
        this.explosionDensityCache.put(key, density);
    }

    public void clearCache() {
        this.explosionDensityCache.clear();
    }
}
