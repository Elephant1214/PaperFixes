package me.elephant1214.paperfixes.mixin.common.lithium.path_node_cache;

import net.minecraft.util.BitArray;
import net.minecraft.world.chunk.BlockStateContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockStateContainer.class)
public interface BlockStateContainerAccessor {
    @Accessor("storage")
    BitArray getStorage();
}
