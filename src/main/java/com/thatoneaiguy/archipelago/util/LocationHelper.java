package com.thatoneaiguy.archipelago.util;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class LocationHelper {
    public static final RegistryKey<World> ARCHIPELAGO = RegistryKey.of(
            RegistryKeys.WORLD,
            new Identifier("archipelago", "archipelago")
    );

    public static boolean isPlayerInArchipelago(ClientPlayerEntity player) {
        return player.getWorld().getRegistryKey().equals(ARCHIPELAGO);
    }

    public static boolean isPlayerInArchipelagoFromWorld(World world) {
        return world.getRegistryKey().equals(ARCHIPELAGO);
    }
}
