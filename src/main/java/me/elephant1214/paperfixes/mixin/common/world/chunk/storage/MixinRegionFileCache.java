package me.elephant1214.paperfixes.mixin.common.world.chunk.storage;

import me.elephant1214.paperfixes.PaperFixes;
import net.minecraft.world.chunk.storage.RegionFile;
import net.minecraft.world.chunk.storage.RegionFileCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

@Mixin(RegionFileCache.class)
public abstract class MixinRegionFileCache {
    @Shadow
    private static final Map<File, RegionFile> REGIONS_BY_FILE = new LinkedHashMap<>(256, 0.75F, true);
    
    @Redirect(
            method = "createOrLoadRegionFile",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/chunk/storage/RegionFileCache;clearRegionFileReferences()V"
            )
    )
    private static synchronized void trimInsteadOfClearing() {
        paperFixes$trimCache();
    }
    
    @Unique
    private static synchronized void paperFixes$trimCache() {
        Iterator<Map.Entry<File, RegionFile>> iterator = REGIONS_BY_FILE.entrySet().iterator();
        int count = REGIONS_BY_FILE.size() - 256;
        while (count-- >= 0 && iterator.hasNext()) {
            try {
                iterator.next().getValue().close();
            } catch (IOException e) {
                PaperFixes.LOGGER.error("Unable to close region file", e);
            }
            iterator.remove();
        }
    }
}
