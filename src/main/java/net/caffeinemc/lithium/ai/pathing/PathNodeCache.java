package net.caffeinemc.lithium.ai.pathing;

import it.unimi.dsi.fastutil.objects.Reference2BooleanMap;
import it.unimi.dsi.fastutil.objects.Reference2BooleanOpenHashMap;
import me.elephant1214.paperfixes.mixin.common.lithium.path_node_cache.BlockStateContainerAccessor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.BitArray;
import net.minecraft.world.chunk.BlockStateContainer;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

public final class PathNodeCache {
    private static final Reference2BooleanMap<ExtendedBlockStorage> chunkNeighborDangerCache = new Reference2BooleanOpenHashMap<>();
    private static boolean dangerCacheEnabled = false;
    private static ExtendedBlockStorage prevQueriedNeighborSectionKey;
    private static boolean prevQueriedNeighborSectionResult;

    public static void enableChunkCache() {
        dangerCacheEnabled = true;
    }

    public static void disableChunkCache() {
        dangerCacheEnabled = false;
        chunkNeighborDangerCache.clear();

        prevQueriedNeighborSectionKey = null;
        prevQueriedNeighborSectionResult = false;
    }

    public static boolean isChunkSectionDangerousNeighbor(ExtendedBlockStorage section) {
        BlockStateContainer palette = section.getData();
        BitArray data = ((BlockStateContainerAccessor) palette).getStorage();
        int size = data.size();
        for (int i = 0; i < size; ++i) {
            if (getNeighborPathNodeType(palette.get(data.getAt(i))) != PathNodeType.OPEN) {
                return true;
            }
        }
        return false;
    }

    public static PathNodeType getPathNodeType(IBlockState state) {
        return ((BlockStatePathingCache) state).paperFixes$getPathNodeType();
    }

    public static PathNodeType getNeighborPathNodeType(IBlockState state) {
        return ((BlockStatePathingCache) state).paperFixes$getNeighborPathNodeType();
    }

    public static boolean isSectionSafeAsNeighbor(ExtendedBlockStorage section) {
        if (section.isEmpty()) {
            return true;
        }

        if (!dangerCacheEnabled) {
            return false;
        }

        if (prevQueriedNeighborSectionKey != section) {
            prevQueriedNeighborSectionKey = section;
            prevQueriedNeighborSectionResult = !chunkNeighborDangerCache.computeIfAbsent(section, PathNodeCache::isChunkSectionDangerousNeighbor);
        }

        return prevQueriedNeighborSectionResult;
    }
}
