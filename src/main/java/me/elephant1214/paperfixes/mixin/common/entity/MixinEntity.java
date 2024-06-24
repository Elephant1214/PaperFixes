package me.elephant1214.paperfixes.mixin.common.entity;

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
public abstract class MixinEntity implements ICommandSender, ICapabilitySerializable<NBTTagCompound> {
    @Unique
    private static final Random SHARED_RANDOM = new Random() {
        private boolean locked = false;

        @Override
        public synchronized void setSeed(long seed) {
            if (!this.locked) {
                super.setSeed(seed);
                locked = true;
            }
        }
    };

    @Redirect(
            method = "<init>",
            at = @At(
                    value = "NEW",
                    target = "()Ljava/util/Random;"
            )
    )
    private Random useSharedRandom() {
        return SHARED_RANDOM;
    }
}
