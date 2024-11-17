package com.thatoneaiguy.archipelago.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.world.World;

public class ArchipelagoEntity extends HostileEntity {
    public ArchipelagoEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }
}
