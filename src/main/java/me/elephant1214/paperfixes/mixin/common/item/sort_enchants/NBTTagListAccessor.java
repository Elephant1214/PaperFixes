package me.elephant1214.paperfixes.mixin.common.item.sort_enchants;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(NBTTagList.class)
public interface NBTTagListAccessor {
    @Accessor("tagList")
    List<NBTBase> getTagList();
}
