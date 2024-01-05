package me.elephant1214.paperfixes.mixin.common;

import net.minecraft.world.gen.ChunkGeneratorOverworld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ChunkGeneratorOverworld.class)
public class MixinChunkGeneratorOverworld {
    @ModifyVariable(
            method = "generateHeightmap",
            at = @At(value = "FIELD", target = "Lnet/minecraft/world/gen/ChunkGeneratorOverworld;biomeWeights:[F"),
            index = 5, allow = 1
    )
    private float mc54738(float f5) {
        return Math.max(f5, -1.8F);
    }
}
