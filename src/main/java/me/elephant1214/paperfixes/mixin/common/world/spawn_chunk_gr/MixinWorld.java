package me.elephant1214.paperfixes.mixin.common.world.spawn_chunk_gr;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.elephant1214.paperfixes.gamerule.SpawnChunkRule;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(World.class)
public abstract class MixinWorld implements IBlockAccess, ICapabilityProvider {
    @Shadow
    public abstract BlockPos getSpawnPoint();

    @ModifyReturnValue(method = "isSpawnChunk", at = @At("RETURN"))
    private boolean keepSpawnLoaded(boolean original, int chunkX, int chunkZ) {
        final int radius = SpawnChunkRule.radius;
        if (radius == 0) return false;

        BlockPos spawn = this.getSpawnPoint();
        final int spawnX = ((spawn.getX() >> 4) * 16) + 8;
        final int spawnZ = ((spawn.getZ() >> 4) * 16) + 8;
        final int chunkMidX = chunkX * 16 + 8 - spawnX;
        final int chunkMidZ = chunkZ * 16 + 8 - spawnZ;

        final int bound = (radius * 16);
        return chunkMidX >= -bound && chunkMidX <= bound && chunkMidZ >= -bound && chunkMidZ <= bound;
    }
}
