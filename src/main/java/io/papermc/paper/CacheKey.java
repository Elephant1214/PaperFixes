package io.papermc.paper;

import me.elephant1214.paperfixes.mixin.common.accessor.AccessorExplosion;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class CacheKey {
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
        long temp;
        result = world.hashCode();
        temp = Double.doubleToLongBits(posX);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(posY);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(posZ);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(minX);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(minY);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(minZ);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(maxX);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(maxY);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(maxZ);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
