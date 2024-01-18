package me.elephant1214.paperfixes.mixin.common.world;

import io.papermc.paper.CacheKey;
import me.elephant1214.paperfixes.PaperFixes;
import me.elephant1214.paperfixes.configuration.PaperFixesConfig;
import net.minecraft.entity.Entity;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(Explosion.class)
public class MixinExplosion {
    @Shadow
    @Final
    private Entity exploder;
    @Shadow
    @Final
    private World world;

    @Redirect(
            method = "doExplosionA",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;getEntitiesWithinAABBExcludingEntity(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/AxisAlignedBB;)Ljava/util/List;"
            )
    )
    private List<Entity> fixExplosionsProcessingDeadEntities(World world, Entity entityIn, AxisAlignedBB bb) {
        return world.getEntitiesInAABBexcluding(this.exploder, bb, entity -> EntitySelectors.CAN_AI_TARGET.apply(entity) && !entity.isDead);
    }

    @Redirect(
            method = "doExplosionA",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;getBlockDensity(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/AxisAlignedBB;)F")
    )
    private float blockDensityCache(World world, Vec3d vector, AxisAlignedBB aabb) {
        if (!PaperFixesConfig.cacheBlockDensities) {
            return this.world.getBlockDensity(vector, aabb);
        }
        CacheKey key = new CacheKey((Explosion) (Object) this, aabb);
        Float blockDensity = PaperFixes.explosionDensityCache.getCached(key);
        if (blockDensity == null) {
            blockDensity = this.world.getBlockDensity(vector, aabb);
            PaperFixes.explosionDensityCache.addCached(key, blockDensity);
        }
        return blockDensity;
    }
}
