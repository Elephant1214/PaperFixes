package me.elephant1214.paperfixes.mixin;

import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import me.elephant1214.paperfixes.configuration.PaperFixesConfig;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class PFMixinConfigPlugin implements IMixinConfigPlugin {
    @Override
    public void onLoad(String mixinPackage) {
        MixinExtrasBootstrap.init();
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        switch (mixinClassName) {
            case "me.elephant1214.paperfixes.mixin.common.world.chunk.MixinChunk":
                if (!PaperFixesConfig.mixinsSection.removeInvalidMobSpawners) return false;
                break;
            case "me.elephant1214.paperfixes.mixin.common.world.chunk.MixinChunkGeneratorOverworld":
                if (!PaperFixesConfig.mixinsSection.mc54738Fix) return false;
                break;
            case "me.elephant1214.paperfixes.mixin.common.world.chunk.MixinChunkProviderServer":
                if (!PaperFixesConfig.mixinsSection.cacheLastChunk) return false;
                break;
            case "me.elephant1214.paperfixes.mixin.common.entity.MixinEntity":
                if (!PaperFixesConfig.mixinsSection.sharedRandomInstanceForEntities) return false;
                break;
            case "me.elephant1214.paperfixes.mixin.common.network.datasync.MixinEntityDataManager":
                if (!PaperFixesConfig.mixinsSection.optimizedEntityDataManagerHashMap) return false;
                break;
            case "me.elephant1214.paperfixes.mixin.common.world.MixinExplosion":
            case "me.elephant1214.paperfixes.mixin.common.world.accessor.AccessorExplosion":
                if (!PaperFixesConfig.mixinsSection.explosionMixin) return false;
                break;
            case "me.elephant1214.paperfixes.mixin.common.world.chunk.MixinExtendedBlockStorage":
                if (!PaperFixesConfig.mixinsSection.mc80966Fix) return false;
                break;
            case "me.elephant1214.paperfixes.mixin.common.server.MixinMinecraftServer":
                if (!PaperFixesConfig.enhancedTickLoop.enabled) return false;
                break;
            case "me.elephant1214.paperfixes.mixin.common.network.MixinNetworkManager":
                if (!PaperFixesConfig.mixinsSection.clearPacketQueueOnDisconnect) return false;
                break;
            case "me.elephant1214.paperfixes.mixin.common.entity.MixinRangedAttribute":
                if (!PaperFixesConfig.mixinsSection.mc133373Fix) return false;
                break;
            case "me.elephant1214.paperfixes.mixin.common.entity.MixinRegionFile":
                if (!PaperFixesConfig.mixinsSection.reduceIoOpsForRegions) return false;
                break;
            case "me.elephant1214.paperfixes.mixin.common.entity.MixinRegionFileCache":
                if (!PaperFixesConfig.mixinsSection.trimRegionCacheInsteadOfClearing) return false;
                break;
            case "me.elephant1214.paperfixes.mixin.common.world.MixinTeleporter":
            case "me.elephant1214.paperfixes.mixin.common.network.invoker.InvokerNetHandlerPlayServer":
                if (!PaperFixesConfig.mixinsSection.mc98153Fix) return false;
                break;
            case "me.elephant1214.paperfixes.mixin.common.tileentity.MixinTileEntity":
                if (!PaperFixesConfig.mixinsSection.preventHangingTileEntityCrashes) return false;
                break;
            case "me.elephant1214.paperfixes.mixin.common.tileentity.MixinTileEntityChest":
                if (!PaperFixesConfig.mixinsSection.removeChestAnimationsFromTick) return false;
                break;
            case "me.elephant1214.paperfixes.mixin.common.world.MixinWorld":
                if (!PaperFixesConfig.mixinsSection.worldMixin) return false;
                break;
        }
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}
