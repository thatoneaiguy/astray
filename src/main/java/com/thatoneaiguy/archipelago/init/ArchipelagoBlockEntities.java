package com.thatoneaiguy.archipelago.init;

import com.thatoneaiguy.archipelago.Archipelago;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

public interface ArchipelagoBlockEntities {
    Map<BlockEntityType<? extends BlockEntity>, Identifier> BLOCK_ENTITIES = new LinkedHashMap<>();

    private static <T extends BlockEntityType<? extends BlockEntity>> T createBlockEntity(String name, T entity) {
        BLOCK_ENTITIES.put(entity, Archipelago.id(name));
        return entity;
    }

    static void initialize() {
        BLOCK_ENTITIES.keySet().forEach(entityType -> Registry.register(Registries.BLOCK_ENTITY_TYPE, BLOCK_ENTITIES.get(entityType), entityType));
    }

}
