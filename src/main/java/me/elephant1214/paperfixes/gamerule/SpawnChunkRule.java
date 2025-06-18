package me.elephant1214.paperfixes.gamerule;

import me.elephant1214.paperfixes.configuration.PaperFixesConfig;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DimensionType;
import net.minecraft.world.GameRules;
import net.minecraftforge.event.GameRuleChangeEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

public final class SpawnChunkRule {
    public static final String SPAWN_CHUNK_RADIUS_RULE = "spawnChunkRadius";
    public static int radius = PaperFixesConfig.features.defaultSpawnChunkRadius;

    /**
     * The gamerule is added when the overworld is loaded.
     * The overworld is always the first dimension to be loaded.
     */
    @SubscribeEvent
    public void worldLoad(@NotNull WorldEvent.Load event) {
        if (event.getWorld().isRemote || event.getWorld().provider.getDimensionType() != DimensionType.OVERWORLD) {
            return;
        }

        GameRules rules = event.getWorld().getGameRules();
        if (!rules.hasRule(SPAWN_CHUNK_RADIUS_RULE)) {
            rules.addGameRule(SPAWN_CHUNK_RADIUS_RULE, Integer.toString(radius), GameRules.ValueType.NUMERICAL_VALUE);
        }
    }

    /**
     * The radius must be reset to the default value defined in the config when the world
     * is unloaded to prevent modified values from carrying over into other worlds.
     */
    @SubscribeEvent
    public void worldUnload(@NotNull WorldEvent.Unload event) {
        if (!event.getWorld().isRemote && event.getWorld().provider.getDimensionType() == DimensionType.OVERWORLD) {
            radius = PaperFixesConfig.features.defaultSpawnChunkRadius;
        }
    }

    /**
     * Updates the radius field when the gamerule is changed.
     * The side does not need to be checked here, this is only triggered on servers.
     */
    @SubscribeEvent
    public void gameRuleUpdate(@NotNull GameRuleChangeEvent event) {
        if (SPAWN_CHUNK_RADIUS_RULE.equals(event.getRuleName())) {
            radius = getRadius(event.getRules());
        }
    }

    private static int getRadius(@NotNull GameRules gameRules) {
        final int spawnChunkRadius = gameRules.getInt(SPAWN_CHUNK_RADIUS_RULE);
        if (spawnChunkRadius > 32 || spawnChunkRadius < 0) {
            final int clamped = MathHelper.clamp(spawnChunkRadius, 0, 32);
            gameRules.setOrCreateGameRule(SPAWN_CHUNK_RADIUS_RULE, Integer.toString(clamped));
            return clamped;
        }

        return spawnChunkRadius;
    }
}
