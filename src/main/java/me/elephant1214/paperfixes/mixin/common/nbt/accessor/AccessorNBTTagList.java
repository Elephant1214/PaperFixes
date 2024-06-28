package me.elephant1214.paperfixes.mixin.common.nbt.accessor;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(NBTTagList.class)
public interface AccessorNBTTagList {
    @Accessor("tagList")
    List<NBTBase> getTagList();
}
