package me.elephant1214.paperfixes.mixin.common.lithium.path_node_cache;

import net.caffeinemc.lithium.LithiumVars;
import net.caffeinemc.lithium.ai.pathing.BlockStatePathingCache;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class BlockMixin {
    @SuppressWarnings("RedundantCast")
    @Inject(method = "registerBlocks", at = @At("RETURN"))
    private static void initPathNodeCache(CallbackInfo ci) {
        LithiumVars.areBlocksInitialized = true;
        for (Block block : Block.REGISTRY) {
            BlockStateContainer stateList = block.getBlockState();
            for (IBlockState state : stateList.getValidStates()) {
                ((BlockStatePathingCache) (BlockStateContainer.StateImplementation) state).paperFixes$initPathTypeCache();
            }
        }
    }
}
