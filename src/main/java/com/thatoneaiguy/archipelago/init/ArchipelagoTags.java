package com.thatoneaiguy.archipelago.init;

import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ArchipelagoTags {
    public static final TagKey<Item> NO_EXIT = TagKey.of(Registry.ITEM_KEY, new Identifier("archipelago", "no_exit"));
}
