package com.thatoneaiguy.archipelago.init;

import com.thatoneaiguy.archipelago.Archipelago;
import com.thatoneaiguy.archipelago.item.*;
import com.thatoneaiguy.archipelago.item.debug.CameraModificationDebugItem;
import com.thatoneaiguy.archipelago.item.debug.RunicTestItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class ArchipelagoItems {
    public static final Item STARWEAVER = registerItem("starweaver", new Starweaver(new FabricItemSettings().rarity(Rarity.EPIC).fireproof()));
    public static final Item ASTROLOGICAL_GREATSWORD = registerItem("astrological_greatsword", new AstrologicalGreatsword(new FabricItemSettings().rarity(Rarity.EPIC).fireproof()));

    public static final Item FORTNITE_EVENT_ITEM = registerItem("fortnite_event_item", new EventItem(new FabricItemSettings().rarity(Rarity.EPIC).fireproof()));
    public static final Item RUPTURE_EVENT_ITEM = registerItem("rupture_event_item", new EventItem(new FabricItemSettings().rarity(Rarity.EPIC).fireproof()));

    public static final Item ENIGMA_SHARD = registerItem("enigma_shard", new Item(new FabricItemSettings().rarity(Rarity.UNCOMMON).fireproof()));

    public static final Item JAR = registerItem("jar", new JarItem(new FabricItemSettings().rarity(Rarity.RARE).maxCount(1)));

    // DEBUG ITEMS
    public static final Item RUNIC_TEST_ITEM = registerItem("runic_test_item", new RunicTestItem(new FabricItemSettings().rarity(Rarity.UNCOMMON).fireproof()));
    public static final Item CAMERA_MODIFICATION_TEST_ITEM = registerItem("camera_modification_test_item", new CameraModificationDebugItem(new FabricItemSettings().rarity(Rarity.UNCOMMON).fireproof()));

    // ARCHI ONLY
        // MOVEMENT
    public static final Item GLIDER = registerItem("glider", new GliderItem(new FabricItemSettings().rarity(Rarity.UNCOMMON).fireproof()));
    public static final Item GRAPPLE_HOOK = registerItem("grapple_hook", new GrappleHookItem(new FabricItemSettings().rarity(Rarity.UNCOMMON).fireproof()));
        // RUNIC
    public static final Item RELIK = registerItem("relik", new Item(new FabricItemSettings().rarity(Rarity.UNCOMMON).fireproof()));


    public static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(Archipelago.MODID, name), item);
    }

    public static void register() {}
}
