package me.elephant1214.paperfixes.mixin.common.world.explosions_ignore_dead;

import net.minecraft.entity.Entity;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(Explosion.class)
public abstract class MixinExplosion {
    @Shadow
    @Final
    private Entity exploder;

    @Redirect(method = "doExplosionA", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getEntitiesWithinAABBExcludingEntity(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/AxisAlignedBB;)Ljava/util/List;"))
    private List<Entity> fixExplosionsIgnoreDeadEntities(World world, Entity entityIn, AxisAlignedBB bb) {
        return world.getEntitiesInAABBexcluding(this.exploder, bb, entity -> EntitySelectors.CAN_AI_TARGET.apply(entity) && !entity.isDead);
    }
}
