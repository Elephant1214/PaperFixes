package me.elephant1214.paperfixes.mixin.common.world;

import io.papermc.paper.CacheKey;
import me.elephant1214.paperfixes.configuration.PaperFixesConfig;
import me.elephant1214.paperfixes.manager.ExplosionDensityCacheManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Explosion.class)
public class MixinExplosion_ExplosionDensity {
    @Shadow
    @Final
    private World world;

    @Redirect(
            method = "doExplosionA",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;getBlockDensity(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/AxisAlignedBB;)F"
            )
    )
    private float blockDensityCache(World world, Vec3d vector, AxisAlignedBB aabb) {
        if (!PaperFixesConfig.INSTANCE.cacheBlockDensities) {
            return this.world.getBlockDensity(vector, aabb);
        }
        CacheKey key = new CacheKey((Explosion) (Object) this, aabb);
        Float blockDensity = ExplosionDensityCacheManager.INSTANCE.getCached(key);
        if (blockDensity == null) {
            blockDensity = this.world.getBlockDensity(vector, aabb);
            ExplosionDensityCacheManager.INSTANCE.addCached(key, blockDensity);
        }
        return blockDensity;
    }
}
