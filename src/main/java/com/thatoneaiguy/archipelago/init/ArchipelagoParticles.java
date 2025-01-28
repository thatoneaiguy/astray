package com.thatoneaiguy.archipelago.init;

import com.thatoneaiguy.archipelago.Archipelago;
import com.thatoneaiguy.archipelago.particle.VaseBreakParticle;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ArchipelagoParticles {
    public static final DefaultParticleType VASE_BREAK_PARTICLE = FabricParticleTypes.simple();
    public static final DefaultParticleType BLOOD = FabricParticleTypes.simple();

    static void registerFactories() {
        ParticleFactoryRegistry.getInstance().register(BLOOD, FlameParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(VASE_BREAK_PARTICLE, VaseBreakParticle.Factory::new);
    }

    public static void register() {
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(Archipelago.MODID, "vase_break_particle"),
                VASE_BREAK_PARTICLE);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(Archipelago.MODID, "blood"),
                BLOOD);
    }
}

