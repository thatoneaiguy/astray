package com.thatoneaiguy.archipelago.entity;

import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleRegistry;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;

import java.awt.*;

public class RiftEntity extends HostileEntity {
    public RiftEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    private static final int CORE_PARTICLES = 10;
    private static final int TENDRIL_PARTICLES = 15;
    private static final float RIFT_RADIUS = 0.3f;
    private static final float EXPANSION_SPEED = 0.02f;
    private static final int PARTICLE_LIFETIME = 30;

    @Override
    public void tick() {
        spawnRiftParticles(getWorld(), getPos());
    }

    public static void spawnRiftParticles(net.minecraft.world.World world, Vec3d pos) {
        Random RANDOM = world.getRandom();
        RegistryObject particleType = LodestoneParticleRegistry.WISP_PARTICLE;

        // **Core Swirling Effect**
        for (int i = 0; i < CORE_PARTICLES; i++) {
            double angle = (2 * Math.PI * i) / CORE_PARTICLES;
            double xOffset = Math.cos(angle) * (RIFT_RADIUS + RANDOM.nextFloat() * 0.1);
            double zOffset = Math.sin(angle) * (RIFT_RADIUS + RANDOM.nextFloat() * 0.1);
            double yOffset = RANDOM.nextFloat() * 0.2 - 0.1; // Small up/down variation

            WorldParticleBuilder.create(particleType)
                    .setScaleData(GenericParticleData.create(0.4f, 0).build())
                    .setTransparencyData(GenericParticleData.create(0.9f, 0.3f).build()) // Flickering effect
                    .setColorData(ColorParticleData.create(new Color(30, 0, 50), new Color(0, 0, 0))
                            .setCoefficient(1.4f)
                            .setEasing(Easing.SINE_IN_OUT)
                            .build())
                    .setSpinData(SpinParticleData.create(0.3f, 0.5f)
                            .setSpinOffset((world.getTime() * 0.1f) % 6.28f)
                            .setEasing(Easing.EXPO_IN_OUT)
                            .build())
                    .setLifetime(PARTICLE_LIFETIME)
                    .setMotion(xOffset * 0.02, yOffset * 0.02, zOffset * 0.02) // Slow swirl motion
                    .enableNoClip()
                    .spawn(world, pos.x + xOffset, pos.y + yOffset, pos.z + zOffset);
        }

        // **Outer Wisps / Tendrils**
        for (int i = 0; i < TENDRIL_PARTICLES; i++) {
            double angle = RANDOM.nextDouble() * Math.PI * 2;
            double speed = EXPANSION_SPEED + RANDOM.nextFloat() * 0.05;
            double xVelocity = Math.cos(angle) * speed;
            double zVelocity = Math.sin(angle) * speed;
            double yVelocity = (RANDOM.nextFloat() - 0.5) * 0.1;

            WorldParticleBuilder.create(particleType)
                    .setScaleData(GenericParticleData.create(0.2f, 0).build())
                    .setTransparencyData(GenericParticleData.create(0.8f, 0.2f).build()) // Wispy effect
                    .setColorData(ColorParticleData.create(new Color(50, 0, 100), new Color(0, 0, 255))
                            .setCoefficient(1.2f)
                            .setEasing(Easing.QUARTIC_OUT)
                            .build())
                    .setSpinData(SpinParticleData.create(0.1f, 0.3f)
                            .setSpinOffset((world.getTime() * 0.15f) % 6.28f)
                            .setEasing(Easing.CUBIC_OUT)
                            .build())
                    .setLifetime(PARTICLE_LIFETIME)
                    .setMotion(xVelocity, yVelocity, zVelocity) // Outward movement
                    .enableNoClip()
                    .spawn(world, pos.x, pos.y, pos.z);
        }
    }
}
