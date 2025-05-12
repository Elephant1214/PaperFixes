package me.elephant1214.paperfixes.gamerule;

import me.elephant1214.paperfixes.configuration.PaperFixesConfig;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameRules;
import net.minecraftforge.event.GameRuleChangeEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

public final class SpawnChunkRule {
    public static final String SPAWN_CHUNK_RADIUS_RULE = "spawnChunkRadius";
    public static int radius = PaperFixesConfig.INSTANCE.defaultSpawnChunkRadius;

    @SubscribeEvent
    public void worldLoad(@NotNull WorldEvent.Load event) {
        if (!event.getWorld().isRemote) {
            GameRules rules = event.getWorld().getGameRules();
            if (!rules.hasRule(SPAWN_CHUNK_RADIUS_RULE)) {
                rules.addGameRule(SPAWN_CHUNK_RADIUS_RULE, Integer.toString(radius), GameRules.ValueType.NUMERICAL_VALUE);
            }
        }
    }

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
