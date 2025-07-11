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
    public boolean shouldApplyMixin(String target, String mixin) {
        if (!mixin.startsWith("me.elephant1214.paperfixes.mixin")) {
            return true;
        }

        if (mixin.contains("multiplayer.cache_last_chunk")) {
            return PaperFixesConfig.client.cacheLastChunk;
        } else if (mixin.contains("fast_chests")) {
            return PaperFixesConfig.performance.fastChests;
        } else if (mixin.contains("fast_border")) {
            return PaperFixesConfig.enableFastBorder();
        } else if (mixin.contains("dont_offload_beacon")) {
            return PaperFixesConfig.bugfixes.dontOffloadBeaconColorUpdate;
        } else if (mixin.contains("shulker_dupe")) {
            return PaperFixesConfig.bugfixes.fixShulkerDupe;
        } else if (mixin.endsWith("RangedAttributeMixin")) {
            return PaperFixesConfig.bugfixes.fixMc133373;
        } else if (mixin.contains("ignore_full_stack")) {
            return PaperFixesConfig.bugfixes.avoidItemMergeForFullStacks;
        } else if (mixin.contains("optimize_pathfinding")) {
            return PaperFixesConfig.performance.optimizePathfinding;
        } else if (mixin.contains("shared_random")) {
            return PaperFixesConfig.performance.sharedRandomForEntities;
        } else if (mixin.contains("water_spawn_check")) {
            return PaperFixesConfig.bugfixes.fixWaterMobSpawnCheck;
        } else if (mixin.contains("sort_enchants")) {
            return PaperFixesConfig.bugfixes.sortEnchantments;
        } else if (mixin.contains("clear_packet_queue")) {
            return PaperFixesConfig.bugfixes.clearPacketQueue;
        } else if (mixin.contains("fast_data_mgr")) {
            return PaperFixesConfig.performance.fastEntityDataMap;
        } else if (mixin.contains("improved_tick_loop")) {
            return PaperFixesConfig.features.improvedTickLoop;
        } else if (mixin.contains("optimize_task_queue")) {
            return PaperFixesConfig.performance.optimizedTaskQueue;
        } else if (mixin.contains("handle_null_tile_crash")) {
            return PaperFixesConfig.bugfixes.handleNullTileCrashes;
        } else if (mixin.endsWith("TeleporterMixin") || mixin.endsWith("NetHandlerPlayServerInvoker")) {
            return PaperFixesConfig.bugfixes.fixMc98153;
        } else if (mixin.endsWith("ExtendedBlockStorageMixin")) {
            return PaperFixesConfig.bugfixes.fixMc80966;
        } else if (mixin.contains("cache_densities")) {
            return PaperFixesConfig.performance.cacheBlockDensities;
        } else if (mixin.contains("common.world.cache_last_chunk")) {
            return PaperFixesConfig.performance.cacheLastChunk;
        } else if (mixin.contains("explosions_ignore_dead")) {
            return PaperFixesConfig.bugfixes.explosionsIgnoreDeadEntities;
        } else if (mixin.contains("spawn_chunk_gr")) {
            return PaperFixesConfig.features.spawnChunkGamerule;
        } else if (mixin.endsWith("ChunkGeneratorOverworldMixin")) {
            return PaperFixesConfig.bugfixes.fixMc54738;
        } else if (mixin.contains("queue_saves")) {
            return PaperFixesConfig.performance.queueChunkSaving;
        } else if (mixin.contains("remove_invalid_spawners")) {
            return PaperFixesConfig.bugfixes.removeInvalidMobSpawners;
        } else if (mixin.contains("smart_region_read")) {
            return PaperFixesConfig.performance.smartRegionRead;
        } else if (mixin.contains("trim_region_cache")) {
            return PaperFixesConfig.performance.trimRegionCache;
        } else if (mixin.contains("compact_lut")) {
            return PaperFixesConfig.performance.compactLut;
        } else if (mixin.contains("path_node_cache")) {
            if (mixin.contains("ChunkCacheMixin")) {
                return PaperFixesConfig.performance.pathNodeCache && !PaperFixesConfig.performance.pathingChunkCache;
            }
            return PaperFixesConfig.performance.pathNodeCache;
        } else if (mixin.contains("pathing_chunk_cache")) {
            return PaperFixesConfig.performance.pathingChunkCache;
        } else if (mixin.contains("dispenser_shulker_crash")) {
            return PaperFixesConfig.bugfixes.fixShulkerDispenseCrash;
        }

        return false;
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
