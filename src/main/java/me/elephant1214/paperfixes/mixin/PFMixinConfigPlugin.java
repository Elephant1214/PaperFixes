package me.elephant1214.paperfixes.mixin;

import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import me.elephant1214.paperfixes.configuration.PaperFixesConfig;
import me.elephant1214.paperfixes.configuration.TickLoopMode;
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
                if (!PaperFixesConfig.bugFixes.removeInvalidMobSpawners) return false;
                break;
            case "me.elephant1214.paperfixes.mixin.common.world.chunk.MixinChunkGeneratorOverworld":
                if (!PaperFixesConfig.bugFixes.mc54738Fix) return false;
                break;
            case "me.elephant1214.paperfixes.mixin.common.world.chunk.MixinChunkProviderServer":
                if (!PaperFixesConfig.performanceFixes.cacheLastChunk) return false;
                break;
            case "me.elephant1214.paperfixes.mixin.common.entity.MixinEntity":
                if (!PaperFixesConfig.performanceFixes.sharedRandomInstanceForEntities) return false;
                break;
            case "me.elephant1214.paperfixes.mixin.common.network.datasync.MixinEntityDataManager":
                if (!PaperFixesConfig.performanceFixes.optimizedEntityDataManagerHashMap) return false;
                break;
            case "me.elephant1214.paperfixes.mixin.common.entity.passive.MixinEntityWaterMob":
                if (!PaperFixesConfig.bugFixes.fixCanSpawnHereCheck) return false;
                break;
            case "me.elephant1214.paperfixes.mixin.common.world.MixinExplosion":
            case "me.elephant1214.paperfixes.mixin.common.world.accessor.AccessorExplosion":
                if (!PaperFixesConfig.performanceFixes.dontProcessDeadEntities) return false;
                break;
            case "me.elephant1214.paperfixes.mixin.common.world.chunk.MixinExtendedBlockStorage":
                if (!PaperFixesConfig.bugFixes.mc80966Fix) return false;
                break;
            case "me.elephant1214.paperfixes.mixin.common.world.MixinExplosion_ExplosionDensity":
            case "me.elephant1214.paperfixes.mixin.common.server.MixinMinecraftServer_ExplosionDensity":
                if (!PaperFixesConfig.cacheBlockDensities) return false;
                break;
            case "me.elephant1214.paperfixes.mixin.common.item.MixinItemStack":
            case "me.elephant1214.paperfixes.mixin.common.nbt.accessor.AccessorNBTTagList":
                if (!PaperFixesConfig.bugFixes.sortEnchantments) return false;
                break;
            case "me.elephant1214.paperfixes.mixin.common.world.MixinWorld_KeepSpawnLoaded":
                if (!PaperFixesConfig.keepSpawnLoaded) return false;
                break;
            case "me.elephant1214.paperfixes.mixin.common.server.MixinMinecraftServer":
                if (PaperFixesConfig.enhancedTickLoopMode == TickLoopMode.OFF) return false;
                break;
            case "me.elephant1214.paperfixes.mixin.common.network.MixinNetworkManager":
                if (!PaperFixesConfig.performanceFixes.clearPacketQueueOnDisconnect) return false;
                break;
            case "me.elephant1214.paperfixes.mixin.common.entity.MixinRangedAttribute":
                if (!PaperFixesConfig.bugFixes.mc133373Fix) return false;
                break;
            case "me.elephant1214.paperfixes.mixin.common.entity.MixinRegionFile":
                if (!PaperFixesConfig.performanceFixes.reduceIoOpsForRegions) return false;
                break;
            case "me.elephant1214.paperfixes.mixin.common.entity.MixinRegionFileCache":
                if (!PaperFixesConfig.performanceFixes.trimRegionCacheInsteadOfClearing) return false;
                break;
            case "me.elephant1214.paperfixes.mixin.common.world.MixinTeleporter":
            case "me.elephant1214.paperfixes.mixin.common.network.invoker.InvokerNetHandlerPlayServer":
                if (!PaperFixesConfig.bugFixes.mc98153Fix) return false;
                break;
            case "me.elephant1214.paperfixes.mixin.common.tileentity.MixinTileEntity":
                if (!PaperFixesConfig.bugFixes.preventHangingTileEntityCrashes) return false;
                break;
            case "me.elephant1214.paperfixes.mixin.common.tileentity.MixinTileEntityChest":
                if (!PaperFixesConfig.performanceFixes.removeChestAnimationsFromTick) return false;
                break;
            case "me.elephant1214.paperfixes.mixin.common.world.MixinWorld":
                if (!PaperFixesConfig.performanceFixes.removeNullTileEntities) return false;
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
