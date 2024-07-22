package io.papermc.paper;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;
import org.jetbrains.annotations.NotNull;

public final class QueuedChunk {
    public final @NotNull ChunkPos pos;
    public final @NotNull NBTTagCompound compound;

    public QueuedChunk(@NotNull ChunkPos pos, @NotNull NBTTagCompound compound) {
        this.pos = pos;
        this.compound = compound;
    }
}
