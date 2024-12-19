package com.thatoneaiguy.archipelago.util;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public class LocationHelper {
    public static final RegistryKey<World> ARCHIPELAGO = RegistryKey.of(
            Registry.WORLD_KEY,
            new Identifier("archipelago", "archipelago")
    );

    public static boolean isPlayerInArchipelago(ClientPlayerEntity player) {
        return player.getWorld().getRegistryKey().equals(ARCHIPELAGO);
    }

    public static boolean isPlayerInArchipelagoFromWorld(World world) {
        return world.getRegistryKey().equals(ARCHIPELAGO);
    }
}
