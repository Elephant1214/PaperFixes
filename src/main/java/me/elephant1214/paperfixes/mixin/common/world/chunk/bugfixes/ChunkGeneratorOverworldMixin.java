package me.elephant1214.paperfixes.mixin.common.world.chunk.bugfixes;

import net.minecraft.world.gen.ChunkGeneratorOverworld;
import net.minecraft.world.gen.IChunkGenerator;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ChunkGeneratorOverworld.class)
public abstract class ChunkGeneratorOverworldMixin implements IChunkGenerator {
    @ModifyVariable(method = "generateHeightmap", at = @At(value = "LOAD", opcode = Opcodes.FLOAD, ordinal = 2), index = 18, allow = 1)
    private float mc54738(float f5) {
        return Math.max(f5, -1.8F);
    }
}
