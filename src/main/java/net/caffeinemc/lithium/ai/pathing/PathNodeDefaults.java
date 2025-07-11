package net.caffeinemc.lithium.ai.pathing;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.PathNodeType;

public class PathNodeDefaults {
    public static PathNodeType getNeighborNodeType(Block block) {
        // [VanillaCopy] LandPathNodeMaker#getNodeTypeFromNeighbors
        // Determine what kind of obstacle type this neighbor is
        if (block == Blocks.CACTUS) {
            return PathNodeType.DANGER_CACTUS;
        } else if (block == Blocks.FIRE) {
            return PathNodeType.DANGER_FIRE;
        } else {
            return PathNodeType.OPEN;
        }
    }

    public static PathNodeType getNodeType(IBlockState state) {
        Block block = state.getBlock();
        Material material = state.getMaterial();
        if (material == Material.AIR) {
            return PathNodeType.OPEN;
        } else if (block != Blocks.TRAPDOOR && block != Blocks.IRON_TRAPDOOR && block != Blocks.WATERLILY) {
            if (block == Blocks.FIRE) {
                return PathNodeType.DAMAGE_FIRE;
            } else if (block == Blocks.CACTUS) {
                return PathNodeType.DAMAGE_CACTUS;
            } else if (block instanceof BlockDoor && material == Material.WOOD && !(Boolean) state.getValue(BlockDoor.OPEN)) {
                return PathNodeType.DOOR_WOOD_CLOSED;
            } else if (block instanceof BlockDoor && material == Material.IRON && !(Boolean) state.getValue(BlockDoor.OPEN)) {
                return PathNodeType.DOOR_IRON_CLOSED;
            } else if (block instanceof BlockDoor && state.getValue(BlockDoor.OPEN)) {
                return PathNodeType.DOOR_OPEN;
            } else if (block instanceof BlockRailBase) {
                return PathNodeType.RAIL;
            } else if (!(block instanceof BlockFence) && !(block instanceof BlockWall) && (!(block instanceof BlockFenceGate) || state.getValue(BlockFenceGate.OPEN))) {
                if (material == Material.WATER) {
                    return PathNodeType.WATER;
                } else if (material == Material.LAVA) {
                    return PathNodeType.LAVA;
                } else {
                    return PathNodeType.OPEN;
                }
            } else {
                return PathNodeType.FENCE;
            }
        } else {
            return PathNodeType.TRAPDOOR;
        }
    }
}
