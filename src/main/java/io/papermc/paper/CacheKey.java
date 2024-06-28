package io.papermc.paper;

import me.elephant1214.paperfixes.mixin.common.world.accessor.AccessorExplosion;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public final class CacheKey {
    private final World world;
    private final double posX, posY, posZ;
    private final double minX, minY, minZ;
    private final double maxX, maxY, maxZ;

    public CacheKey(Explosion explosion, AxisAlignedBB aabb) {
        this.world = ((AccessorExplosion) explosion).getWorld();
        this.posX = explosion.getPosition().x;
        this.posY = explosion.getPosition().y;
        this.posZ = explosion.getPosition().z;
        this.minX = aabb.minX;
        this.minY = aabb.minY;
        this.minZ = aabb.minZ;
        this.maxX = aabb.maxX;
        this.maxY = aabb.maxY;
        this.maxZ = aabb.maxZ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CacheKey cacheKey = (CacheKey) o;

        if (Double.compare(cacheKey.posX, posX) != 0) return false;
        if (Double.compare(cacheKey.posY, posY) != 0) return false;
        if (Double.compare(cacheKey.posZ, posZ) != 0) return false;
        if (Double.compare(cacheKey.minX, minX) != 0) return false;
        if (Double.compare(cacheKey.minY, minY) != 0) return false;
        if (Double.compare(cacheKey.minZ, minZ) != 0) return false;
        if (Double.compare(cacheKey.maxX, maxX) != 0) return false;
        if (Double.compare(cacheKey.maxY, maxY) != 0) return false;
        if (Double.compare(cacheKey.maxZ, maxZ) != 0) return false;
        return world.equals(cacheKey.world);
    }

    @Override
    public int hashCode() {
        int result;
        result = world.hashCode();
        result = 31 * result + Double.hashCode(posX);
        result = 31 * result + Double.hashCode(posY);
        result = 31 * result + Double.hashCode(posZ);
        result = 31 * result + Double.hashCode(minX);
        result = 31 * result + Double.hashCode(minY);
        result = 31 * result + Double.hashCode(minZ);
        result = 31 * result + Double.hashCode(maxX);
        result = 31 * result + Double.hashCode(maxY);
        result = 31 * result + Double.hashCode(maxZ);
        return result;
    }
}
