package com.thatoneaiguy.archipelago.init;

import com.thatoneaiguy.archipelago.world.gen.BridgeFeature;
import com.thatoneaiguy.archipelago.world.gen.IslandChunkGenerator;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

public class ArchipelagoWorldGen {
    public static Feature<DefaultFeatureConfig> BRIDGE_FEATURE;

    public static void register() {
        BRIDGE_FEATURE = Registry.register(
                Registries.FEATURE,
                new Identifier("archipelago", "bridge_feature"),
                new BridgeFeature(DefaultFeatureConfig.CODEC, 0L)
        );

        Registry.register(
                Registries.CHUNK_GENERATOR,
                new Identifier("archipelago", "island_gen"),
                IslandChunkGenerator.CODEC
        );
    }
}
