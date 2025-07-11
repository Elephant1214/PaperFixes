package me.elephant1214.paperfixes.mixin.common.entity.shared_random;

import me.elephant1214.paperfixes.PaperFixes;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

@Mixin(Entity.class)
public abstract class EntityMixin implements ICommandSender, ICapabilitySerializable<NBTTagCompound> {
    @Unique
    private static final Random SHARED_RANDOM = new Random() {
        private boolean locked = false;

        @Override
        public synchronized void setSeed(long seed) {
            if (this.locked) {
                PaperFixes.LOGGER.error("Ignoring set seed on Entity.SHARED_RANDOM", new Throwable());
            } else {
                super.setSeed(seed);
                locked = true;
            }
        }
    };

    @Redirect(method = "<init>", at = @At(value = "NEW", target = "java/util/Random", remap = false))
    private Random useSharedRandom() {
        return SHARED_RANDOM;
    }
}
