package com.thatoneaiguy.archipelago.init;

import com.thatoneaiguy.archipelago.Archipelago;
import com.thatoneaiguy.archipelago.entity.RiftEntity;
import com.thatoneaiguy.archipelago.entity.runic.TotemEntity;
import com.thatoneaiguy.archipelago.entity.runic.acolyte.AcolyteBloodEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ArchipelagoEntities {
    /*public static final EntityType<GooberEntity> GOOBER_ENTITY_TYPE = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(Archipelago.MODID, "goober"),
            FabricEntityTypeBuilder.<GooberEntity>create(SpawnGroup.MISC, GooberEntity::new)
                    .dimensions(EntityDimensions.fixed(0.5F, 0.5F))
                    .fireImmune()
                    .build());*/
    public static final EntityType<RiftEntity> RIFT_ENTITY_TYPE = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Archipelago.MODID, "rift"),
            FabricEntityTypeBuilder.<RiftEntity>create(SpawnGroup.MISC, RiftEntity::new)
                    .dimensions(EntityDimensions.fixed(2F, 2F))
                    .fireImmune()
                    .build());

    // ABILITES
    public static final EntityType<TotemEntity> TOTEM_ENTITY_TYPE = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Archipelago.MODID, "totem"),
            FabricEntityTypeBuilder.<TotemEntity>create(SpawnGroup.MISC, TotemEntity::new)
                    .dimensions(EntityDimensions.fixed(2F, 2F))
                    .fireImmune()
                    .build());
    public static final EntityType<AcolyteBloodEntity> BLOOD_ENTITY_TYPE = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Archipelago.MODID, "blood"),
            FabricEntityTypeBuilder.<AcolyteBloodEntity>create(SpawnGroup.MISC, AcolyteBloodEntity::new)
                    .dimensions(EntityDimensions.fixed(0.3F, 0.3F))
                    .fireImmune()
                    .build());

    public static void register() {}
}
