package me.elephant1214.paperfixes.core;

import me.elephant1214.paperfixes.configuration.PaperFixesConfig;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class PFMixinConfigPlugin implements IMixinConfigPlugin {
    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public @Nullable String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        switch (mixinClassName) {
            case "me.elephant1214.paperfixes.mixin.common.block.MixinBlockBeacon":
            case "me.elephant1214.paperfixes.mixin.common.block.MixinBlockBeacon$1":
                if (!PaperFixesConfig.INSTANCE.bugfixes.dontOffloadBeaconColorUpdate) return false;
                break;
            case "me.elephant1214.paperfixes.mixin.common.block.MixinBlockShulkerBox":
                if (!PaperFixesConfig.INSTANCE.bugfixes.fixShulkerDupe) return false;
                break;
            case "me.elephant1214.paperfixes.mixin.common.entity.MixinEntity":
                if (!PaperFixesConfig.INSTANCE.performance.useSharedRandomForEntities) return false;
                break;
            case "me.elephant1214.paperfixes.mixin.common.entity.MixinRangedAttribute":
                if (!PaperFixesConfig.INSTANCE.bugfixes.fixMc133373) return false;
                break;
            case "me.elephant1214.paperfixes.mixin.common.entity.passive.MixinEntitySquid":
                if (!PaperFixesConfig.INSTANCE.performance.removeSquidSetSeedCalls) return false;
                break;
            case "me.elephant1214.paperfixes.mixin.common.entity.passive.MixinEntityWaterMob":
                if (!PaperFixesConfig.INSTANCE.bugfixes.fixWaterMobSpawnCheck) return false;
                break;
            case "me.elephant1214.paperfixes.mixin.common.item.MixinItemStack":
            case "me.elephant1214.paperfixes.mixin.common.nbt.accessor.AccessorNBTTagList":
                if (!PaperFixesConfig.INSTANCE.bugfixes.sortEnchantments) return false;
                break;
            case "me.elephant1214.paperfixes.mixin.common.network.MixinNetworkManager":
                if (!PaperFixesConfig.INSTANCE.bugfixes.clearPacketQueue) return false;
                break;
            case "me.elephant1214.paperfixes.mixin.common.network.datasync.MixinEntityDataManager":
                if (!PaperFixesConfig.INSTANCE.performance.optimizedEntityDataMap) return false;
                break;
            case "me.elephant1214.paperfixes.mixin.common.server.MixinMinecraftServer":
                if (!PaperFixesConfig.INSTANCE.features.improvedTickLoop) return false;
                break;
            case "me.elephant1214.paperfixes.mixin.common.tileentity.MixinTileEntity":
                if (!PaperFixesConfig.INSTANCE.bugfixes.preventHangingTeCrashes) return false;
                break;
            case "me.elephant1214.paperfixes.mixin.common.world.MixinExplosion":
            case "me.elephant1214.paperfixes.mixin.common.world.accessor.AccessorExplosion":
                if (!PaperFixesConfig.INSTANCE.bugfixes.stopDeadEntityProcessing) return false;
                break;
            case "me.elephant1214.paperfixes.mixin.common.world.MixinExplosion_ExplosionDensity":
            case "me.elephant1214.paperfixes.mixin.common.server.MixinMinecraftServer_ExplosionDensity":
                if (!PaperFixesConfig.INSTANCE.performance.cacheBlockDensities) return false;
                break;
            case "me.elephant1214.paperfixes.mixin.common.world.MixinTeleporter":
            case "me.elephant1214.paperfixes.mixin.common.network.invoker.InvokerNetHandlerPlayServer":
                if (!PaperFixesConfig.INSTANCE.bugfixes.fixMc98153) return false;
                break;
            case "me.elephant1214.paperfixes.mixin.common.world.MixinWorld":
                if (!PaperFixesConfig.INSTANCE.bugfixes.removeNullTileEntities) return false;
                break;
            case "me.elephant1214.paperfixes.mixin.common.world.MixinWorld_SpawnChunkGamerule":
                if (!PaperFixesConfig.INSTANCE.features.enableSpawnChunkGamerule) return false;
                break;
            case "me.elephant1214.paperfixes.mixin.common.world.chunk.MixinChunk":
                if (!PaperFixesConfig.INSTANCE.bugfixes.removeInvalidMobSpawners) return false;
                break;
            case "me.elephant1214.paperfixes.mixin.common.world.chunk.MixinChunkGeneratorOverworld":
                if (!PaperFixesConfig.INSTANCE.bugfixes.fixMc54738) return false;
                break;
            case "me.elephant1214.paperfixes.mixin.common.world.chunk.MixinExtendedBlockStorage":
                if (!PaperFixesConfig.INSTANCE.bugfixes.fixMc80966) return false;
                break;
            case "me.elephant1214.paperfixes.mixin.common.world.chunk.storage.MixinAnvilChunkLoader":
                if (!PaperFixesConfig.INSTANCE.performance.queueChunkSaving) return false;
                break;
            case "me.elephant1214.paperfixes.mixin.common.world.chunk.storage.MixinRegionFile":
                if (!PaperFixesConfig.INSTANCE.performance.reduceRegionIoOps) return false;
                break;
            case "me.elephant1214.paperfixes.mixin.common.world.chunk.storage.MixinRegionFileCache":
                if (!PaperFixesConfig.INSTANCE.performance.trimRegionCache) return false;
                break;
            case "me.elephant1214.paperfixes.mixin.common.world.gen.MixinChunkProviderServer":
                if (!PaperFixesConfig.INSTANCE.performance.cacheLastChunk) return false;
                break;

            // Don't need to check side here because these won't be loaded anyway depending on the side
            case "me.elephant1214.paperfixes.mixin.client.multiplayer.MixinChunkProviderClient":
                if (!PaperFixesConfig.INSTANCE.performance.clientCacheLastChunk) return false;
                break;
            case "me.elephant1214.paperfixes.mixin.server.tileentity.MixinTileEntityChest":
                if (!PaperFixesConfig.INSTANCE.performance.dontHandleChestAnimInTick) return false;
                break;
        }

        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public @Nullable List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}
