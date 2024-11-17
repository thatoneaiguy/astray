package com.thatoneaiguy.archipelago.util;

import com.thatoneaiguy.archipelago.entity.runic.TotemEntity;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TotemManager {
    private static final HashMap<UUID, List<TotemEntity>> playerTotems = new HashMap<>();

    public static void addTotem(ServerPlayerEntity player, TotemEntity totem) {
        UUID playerUUID = player.getUuid();
        List<TotemEntity> totems = playerTotems.getOrDefault(playerUUID, new ArrayList<>());

        if (totems.size() >= 1) {
            TotemEntity oldestTotem = getOldestTotem(totems);
            if (oldestTotem != null) {
                oldestTotem.remove(Entity.RemovalReason.DISCARDED);
                totems.remove(oldestTotem);
            }
        }

        totems.add(totem);
        playerTotems.put(playerUUID, totems);
    }

    private static TotemEntity getOldestTotem(List<TotemEntity> totems) {
        TotemEntity oldestTotem = null;
        long oldestTime = Long.MAX_VALUE;

        for (TotemEntity totem : totems) {
            if (totem.getCreationTime() < oldestTime) {
                oldestTotem = totem;
                oldestTime = totem.getCreationTime();
            }
        }

        return oldestTotem;
    }
}
