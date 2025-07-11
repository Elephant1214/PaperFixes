package me.elephant1214.paperfixes.mixin.common.lithium.path_node_cache;

import net.caffeinemc.lithium.LithiumVars;
import net.caffeinemc.lithium.ai.pathing.BlockStatePathingCache;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.registry.RegistryNamespacedDefaultedByKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RegistryNamespacedDefaultedByKey.class)
public class RegistryNamespacedDefaultedByKeyMixin<K, V> {
    @SuppressWarnings("RedundantCast")
    @Inject(method = "register", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/registry/RegistryNamespaced;register(ILjava/lang/Object;Ljava/lang/Object;)V"))
    private void initPathNodeCache(int id, K key, V value, CallbackInfo ci) {
        if (value instanceof Block && LithiumVars.areBlocksInitialized) {
            Block block = (Block) value;
            BlockStateContainer stateList = block.getBlockState();
            for (IBlockState state : stateList.getValidStates()) {
                ((BlockStatePathingCache) (BlockStateContainer.StateImplementation) state).paperFixes$initPathTypeCache();
            }
        }
    }
}
