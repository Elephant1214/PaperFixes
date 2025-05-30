package me.elephant1214.paperfixes.mixin.server.world.cache_border;

import me.elephant1214.paperfixes.util.MixinUpdateHook;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(World.class)
public abstract class MixinWorld implements IBlockAccess, ICapabilityProvider {
    @Shadow
    public abstract WorldBorder getWorldBorder();

    @Inject(method = "tick", at = @At("HEAD"))
    private void updateBorderIfNeeded(CallbackInfo ci) {
        ((MixinUpdateHook) this.getWorldBorder()).paperFixes$update();
    }
}
