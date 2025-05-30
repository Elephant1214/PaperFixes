package me.elephant1214.paperfixes.manager;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.border.WorldBorder;

import javax.vecmath.Vector2d;

/**
 * The way the game handles the world border is not very good and can get expensive with
 * lots of calls to {@link WorldBorder#contains(BlockPos)} because it recomputes the
 * border every time min/max methods are called.
 * This basically just caches the corners, and two mixins handle updating them when needed.
 */
public final class SmartWorldBorder {
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

    public void recompute() {
        min.set(computeBound(border.getCenterX(), true), computeBound(border.getCenterZ(), true));
        max.set(computeBound(border.getCenterX(), false), computeBound(border.getCenterZ(), false));
    }

    public SmartWorldBorder(final WorldBorder border) {
        this.border = border;
        recompute();
    }

    private double computeBound(final double centerPos, final boolean isMin) {
        final double radius = border.getDiameter() / 2.0;
        final double bound = centerPos + (isMin ? -radius : radius);

        final double limit = border.getSize();
        final double maxAllowed = isMin ? -limit : limit;

        return isMin ? Math.max(bound, maxAllowed) : Math.min(bound, maxAllowed);
    }
}
