package com.thatoneaiguy.archipelago.init;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;


public class ArchipelagoTags {
    public static final TagKey<Item> NO_EXIT = TagKey.of(Registries.ITEM.getKey(), new Identifier("archipelago", "no_exit"));
}
