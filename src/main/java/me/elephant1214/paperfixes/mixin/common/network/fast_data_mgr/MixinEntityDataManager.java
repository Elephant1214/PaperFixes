package me.elephant1214.paperfixes.mixin.common.network.fast_data_mgr;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.network.datasync.EntityDataManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@SuppressWarnings("unused")
@Mixin(EntityDataManager.class)
public abstract class MixinEntityDataManager {
    @Shadow
    private final Map<Integer, EntityDataManager.DataEntry<?>> entries = new Int2ObjectOpenHashMap<>();
}
