package com.thatoneaiguy.archipelago.init;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.entry.RegistryEntry;
import org.jetbrains.annotations.Nullable;

public class ArchipelagoDamageSources extends DamageSource {
    public ArchipelagoDamageSources(RegistryEntry<DamageType> type, @Nullable Entity source, @Nullable Entity attacker) {
        super(type, source, attacker);
    }
    /*public static final DamageSource COMPRESSION = new ArchipelagoDamageSources("compression").setBypassesArmor().setUnblockable();

    public ArchipelagoDamageSources(String name) {
        super(name);
    }*/

    public static void register() {}
}
