package me.elephant1214.paperfixes.mixin.common.network.datasync;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.network.datasync.EntityDataManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@SuppressWarnings("unused")
@Mixin(EntityDataManager.class)
public abstract class MixinEntityDataManager {
    @Shadow @Final
    private final Map<Integer, EntityDataManager.DataEntry<?>> entries = new Int2ObjectOpenHashMap<>();
}
