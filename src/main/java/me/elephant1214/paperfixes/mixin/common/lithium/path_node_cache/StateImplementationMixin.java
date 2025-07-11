package me.elephant1214.paperfixes.mixin.common.lithium.path_node_cache;

import net.caffeinemc.lithium.ai.pathing.BlockStatePathingCache;
import net.caffeinemc.lithium.ai.pathing.PathNodeDefaults;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.pathfinding.PathNodeType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BlockStateContainer.StateImplementation.class)
public abstract class StateImplementationMixin implements BlockStatePathingCache {
    @Shadow
    public abstract Block getBlock();

    @Unique
    private PathNodeType paperFixes$pathNodeType = PathNodeType.OPEN;
    @Unique
    private PathNodeType paperFixes$pathNodeTypeNeighbor = PathNodeType.OPEN;

    @Override
    public PathNodeType paperFixes$getPathNodeType() {
        return this.paperFixes$pathNodeType;
    }

    @Override
    public PathNodeType paperFixes$getNeighborPathNodeType() {
        return this.paperFixes$pathNodeTypeNeighbor;
    }

    @Override
    public void paperFixes$initPathTypeCache() {
        this.paperFixes$pathNodeType = PathNodeDefaults.getNodeType((IBlockState) this);
        this.paperFixes$pathNodeTypeNeighbor = PathNodeDefaults.getNeighborNodeType(getBlock());
    }
}
