package me.elephant1214.paperfixes.mixin.common.world.spawn_chunk_gr;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import me.elephant1214.paperfixes.PaperFixes;
import me.elephant1214.paperfixes.gamerule.SpawnChunkRule;
import net.minecraft.command.ICommandSender;
import net.minecraft.profiler.ISnooperInfo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin implements ICommandSender, Runnable, IThreadListener, ISnooperInfo {
    @Shadow
    public abstract WorldServer getWorld(int dimension);

    @Unique
    private int paperFixes$spawnRadius = -1;

    @WrapWithCondition(method = "initialWorldChunkLoad", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;info(Ljava/lang/String;)V", remap = false))
    private boolean replaceWithPFLogger(Logger instance, String s) {
        this.paperFixes$spawnRadius = SpawnChunkRule.getRadius(this.getWorld(0).getGameRules());
        if (this.paperFixes$spawnRadius == 0) {
            PaperFixes.LOGGER.info("Skipping spawn area pregen");
            return false;
        } else {
            return true;
        }
    }

    @ModifyConstant(method = "initialWorldChunkLoad", constant = @Constant(intValue = 192))
    private int skipSpawnChunksPos(int constant) {
        return this.paperFixes$spawnRadius * 16;
    }

    @ModifyConstant(method = "initialWorldChunkLoad", constant = @Constant(intValue = -192))
    private int skipSpawnChunksNeg(int constant) {
        return this.paperFixes$spawnRadius * 16;
    }
}
