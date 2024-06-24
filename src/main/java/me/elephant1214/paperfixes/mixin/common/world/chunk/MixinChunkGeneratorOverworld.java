package me.elephant1214.paperfixes.mixin.common.world.chunk;

import net.minecraft.world.gen.ChunkGeneratorOverworld;
import net.minecraft.world.gen.IChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ChunkGeneratorOverworld.class)
public abstract class MixinChunkGeneratorOverworld implements IChunkGenerator {
    @ModifyVariable(
            method = "generateHeightmap",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/gen/ChunkGeneratorOverworld;biomeWeights:[F"
            ),
            index = 5, allow = 1
    )
    private float mc54738(float f5) {
        return Math.max(f5, -1.8F);
    }
}
