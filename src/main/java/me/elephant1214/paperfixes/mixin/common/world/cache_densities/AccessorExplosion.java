package me.elephant1214.paperfixes.mixin.common.world.cache_densities;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * See {@link io.papermc.paper.CacheKey#CacheKey(Explosion, AxisAlignedBB)}
 */
@Mixin(Explosion.class)
public interface AccessorExplosion {
    @Accessor("world")
    World getWorld();
}
