package me.elephant1214.paperfixes.mixin.common.world.fast_border;

import me.elephant1214.paperfixes.manager.FastWorldBorder;
import net.minecraft.world.border.WorldBorder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(WorldBorder.class)
public abstract class WorldBorderMixin {
    /**
     * @author Elephant_1214
     * @reason Faster world border
     */
    @Overwrite
    public double minX() {
        return FastWorldBorder.instance().minX();
    }

    /**
     * @author Elephant_1214
     * @reason Faster world border
     */
    @Overwrite
    public double minZ() {
        return FastWorldBorder.instance().minZ();
    }

    /**
     * @author Elephant_1214
     * @reason Faster world border
     */
    @Overwrite
    public double maxX() {
        return FastWorldBorder.instance().maxX();
    }

    /**
     * @author Elephant_1214
     * @reason Faster world border
     */
    @Overwrite
    public double maxZ() {
        return FastWorldBorder.instance().maxZ();
    }
}
