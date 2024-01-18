package me.elephant1214.paperfixes.mixin.common.world.accessor;

import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Explosion.class)
public interface AccessorExplosion {
    @Accessor("world")
    World getWorld();
}
