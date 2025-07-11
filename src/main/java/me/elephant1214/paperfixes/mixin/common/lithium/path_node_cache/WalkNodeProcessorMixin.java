package me.elephant1214.paperfixes.mixin.common.lithium.path_node_cache;

import me.elephant1214.paperfixes.PaperFixes;
import net.caffeinemc.lithium.WorldHelper;
import net.caffeinemc.lithium.ai.pathing.ChunkAccessHelper;
import net.caffeinemc.lithium.ai.pathing.PathNodeCache;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(WalkNodeProcessor.class)
public abstract class WalkNodeProcessorMixin {
    @Shadow
    public abstract void init(IBlockAccess sourceIn, EntityLiving mob);

    /**
     * @author jellysquid3, ruViolence, Elephant_1214
     * @reason Use optimized method
     */
    @Overwrite
    protected PathNodeType getPathNodeTypeRaw(IBlockAccess blockAccess, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        IBlockState state = blockAccess.getBlockState(pos);
        PathNodeType type = PathNodeCache.getPathNodeType(state);

        if (type == PathNodeType.OPEN) {
            Block block = state.getBlock();
            if (!block.isPassable(blockAccess, pos)) {
                return PathNodeType.BLOCKED;
            }

            return PathNodeType.OPEN;
        }

        return type;
    }

    /**
     * @author jellysquid3, ruViolence, Elephant_1214
     * @reason Use optimized method
     */
    @Overwrite
    public PathNodeType checkNeighborBlocks(IBlockAccess blockAccess, int x, int y, int z, PathNodeType type) {
        ExtendedBlockStorage section = null;

        if (WorldHelper.areNeighborsWithinSameChunk(x, y, z)) {
            if (y >= 0 && y < 256) {
                if (blockAccess instanceof WorldServer) {
                    Chunk chunk = ((WorldServer) blockAccess).getChunk(x >> 4, z >> 4);
                    section = chunk.getBlockStorageArray()[y >> 4];
                } else if (blockAccess instanceof ChunkCache) {
                    Chunk chunk = ((ChunkAccessHelper) blockAccess).paperFixes$getChunkAt(x >> 4, z >> 4);

                    if (chunk != null) {
                        section = chunk.getBlockStorageArray()[y >> 4];
                    }
                } else {
                    PaperFixes.LOGGER.error("w h a t");
                }
            }

            if (section == null || section.isEmpty() || PathNodeCache.isSectionSafeAsNeighbor(section)) {
                return type;
            }
        }

        for (int z2 = -1; z2 <= 1; ++z2) {
            for (int x2 = -1; x2 <= 1; ++x2) {
                if (x2 == 0 && z2 == 0) {
                    continue;
                }

                IBlockState state;

                if (section != null) {
                    state = section.get((x2 + x) & 15, y & 15, (z2 + z) & 15);
                } else {
                    state = blockAccess.getBlockState(new BlockPos(x2 + x, y, z2 + z));
                }

                if (state.getBlock() == Blocks.AIR) {
                    continue;
                }

                PathNodeType neighborType = PathNodeCache.getNeighborPathNodeType(state);

                if (neighborType != PathNodeType.OPEN) {
                    return neighborType;
                }
            }
        }

        return type;
    }

}
