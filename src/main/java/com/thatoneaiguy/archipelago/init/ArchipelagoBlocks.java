package com.thatoneaiguy.archipelago.init;

import com.thatoneaiguy.archipelago.Archipelago;
import com.thatoneaiguy.archipelago.block.ResentmentSink;
import com.thatoneaiguy.archipelago.block.PrivacyGlassBlock;
import com.thatoneaiguy.archipelago.block.PrivacyGlassPanelBlock;
//import com.thatoneaiguy.archipelago.world.feature.tree.CrystalSaplingGenerator;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ArchipelagoBlocks {

    public static final Block RESENTMENT_SINK = new ResentmentSink(FabricBlockSettings.copy(Blocks.NETHERITE_BLOCK).requiresTool().strength(1.5F, 6.0F).sounds(BlockSoundGroup.NETHERITE));

    public static final Block PRIVACY_GLASS = new PrivacyGlassBlock(FabricBlockSettings.copyOf(Blocks.BLACK_STAINED_GLASS).nonOpaque());
    public static final Block PRIVACY_GLASS_PANEL = new PrivacyGlassPanelBlock(FabricBlockSettings.copyOf(Blocks.BLACK_STAINED_GLASS).sounds(BlockSoundGroup.GLASS).nonOpaque());

    public static final Block CRYSTAL_LOG = new PillarBlock(FabricBlockSettings.copy(Blocks.OAK_LOG));
    public static final Block CRYSTAL_WOOD = new PillarBlock(FabricBlockSettings.copy(Blocks.OAK_WOOD));
    public static final Block STRIPPED_CRYSTAL_LOG = new PillarBlock(FabricBlockSettings.copy(Blocks.STRIPPED_OAK_LOG));
    public static final Block STRIPPED_CRYSTAL_WOOD = new PillarBlock(FabricBlockSettings.copy(Blocks.STRIPPED_OAK_WOOD));

    public static final Block CRYSTAL_PLANKS = new Block(FabricBlockSettings.copy(Blocks.OAK_PLANKS));
    public static final Block CRYSTAL_LEAVES = new LeavesBlock(FabricBlockSettings.copy(Blocks.OAK_LEAVES));

    //public static final Block CRYSTAL_SAPLING = new SaplingBlock(new CrystalSaplingGenerator(), FabricBlockSettings.copy(Blocks.OAK_SAPLING));

    static Map<String, Object> BLOCKS = Stream.of(new Object[][] {
            {"resentment_sink", RESENTMENT_SINK},

            {"privacy_glass", PRIVACY_GLASS},
            {"privacy_glass_panel", PRIVACY_GLASS_PANEL},

            {"crystal_log", CRYSTAL_LOG},
            {"crystal_wood", CRYSTAL_WOOD},
            {"stripped_crystal_log", STRIPPED_CRYSTAL_LOG},
            {"stripped_crystal_wood", STRIPPED_CRYSTAL_WOOD},

            {"crystal_planks", CRYSTAL_PLANKS},
            {"crystal_leaves", CRYSTAL_LEAVES},

            //{"crystal_sapling", CRYSTAL_SAPLING}

    }).collect(Collectors.toMap(entry -> (String) entry[0], entry -> entry[1]));

    public static void registerAll() {
        for (Map.Entry<String, Object> entry : BLOCKS.entrySet()) {
            String key = entry.getKey();
            Block value = (Block) entry.getValue();

            registerBlock(key, value);
        }
    }

    private static void registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        Registry.register(Registries.BLOCK, new Identifier(Archipelago.MODID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, new Identifier(Archipelago.MODID, name),
                new BlockItem(block, new FabricItemSettings()));
    }

    private static void registerBlockWithoutBlockItem(String name, Block block) {
        Registry.register(Registries.BLOCK, new Identifier(Archipelago.MODID, name), block);
    }
}