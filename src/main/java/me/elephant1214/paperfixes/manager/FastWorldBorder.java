package me.elephant1214.paperfixes.manager;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.border.IBorderListener;
import net.minecraft.world.border.WorldBorder;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.jetbrains.annotations.NotNull;

import javax.vecmath.Vector2d;

/**
 * The way the game handles the world border is not very good and can get expensive with
 * lots of calls to {@link WorldBorder#contains(BlockPos)} because it recomputes the
 * border every time min/max methods are called.
 * This basically just caches the corners, and two mixins handle updating them when needed.
 */
public final class FastWorldBorder {
    private static final double MIN_POS = 1.0;
    private final WorldBorder border;
    /**
     * -X and -Z corner, to the north-west
     */
    private final Vector2d min = new Vector2d();
    /**
     * +X and +Z corner, to the south-east
     */
    private final Vector2d max = new Vector2d();

    public double minX() {
        return this.min.x;
    }

    public double minZ() {
        return this.min.y;
    }

    public double maxX() {
        return this.max.x;
    }

    public double maxZ() {
        return this.max.y;
    }

    /**
     * Recomputes the corners of the border.
     */
    public void recompute() {
        final double radius = border.getDiameter() / 2.0;
        final double centerX = border.getCenterX();
        final double centerZ = border.getCenterZ();

        this.min.set(clampPos(centerX - radius), clampPos(centerZ - radius));
        this.max.set(clampPos(centerX + radius), clampPos(centerZ + radius));
    }

    /**
     * Helper to clamp the final position between (-)1 and the world size.
     */
    private double clampPos(double limit) {
        if (limit < 0) {
            return MathHelper.clamp(limit, -border.getSize(), -MIN_POS);
        } else {
            return MathHelper.clamp(limit, MIN_POS, border.getSize());
        }
    }

    private FastWorldBorder(WorldBorder border) {
        this.border = border;
        recompute();
    }

    private static FastWorldBorder INSTANCE;

    public static FastWorldBorder instance() {
        return INSTANCE;
    }

    public static class Events {
        @SubscribeEvent(priority = EventPriority.HIGHEST)
        public void setupBorder(@NotNull WorldEvent.Load event) {
            World world = event.getWorld();
            if (world.provider.getDimensionType() != DimensionType.OVERWORLD) {
                return;
            }

            WorldBorder border = world.getWorldBorder();
            INSTANCE = new FastWorldBorder(border);
            //<editor-fold desc="Border update listener">
            border.addListener(new IBorderListener() {
                @Override
                public void onSizeChanged(@NotNull WorldBorder border, double newSize) {
                    INSTANCE.recompute();
                }

                @Override
                public void onTransitionStarted(@NotNull WorldBorder border, double oldSize, double newSize, long time) {
                    INSTANCE.recompute();
                }

                @Override
                public void onCenterChanged(@NotNull WorldBorder border, double x, double z) {
                    INSTANCE.recompute();
                }

                @Override
                public void onWarningTimeChanged(@NotNull WorldBorder border, int newTime) {
                }

                @Override
                public void onWarningDistanceChanged(@NotNull WorldBorder border, int newDistance) {
                }

                @Override
                public void onDamageAmountChanged(@NotNull WorldBorder border, double newAmount) {
                }

                @Override
                public void onDamageBufferChanged(@NotNull WorldBorder border, double newSize) {
                }
            });
            //</editor-fold>
        }

        @SubscribeEvent(priority = EventPriority.HIGHEST)
        public void updateBorder(@NotNull TickEvent.WorldTickEvent event) {
            if (event.phase != TickEvent.Phase.START || event.world.provider.getDimensionType() != DimensionType.OVERWORLD) {
                return;
            }

            // Ensures that update events get called if the border is moving
            event.world.getWorldBorder().getDiameter();
        }

        @Mod.EventHandler
        public void unloadBorder(@NotNull FMLServerStoppingEvent event) {
            INSTANCE = null;
        }
    }
}
