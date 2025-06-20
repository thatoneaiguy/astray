package com.thatoneaiguy.archipelago.entity;

import com.thatoneaiguy.archipelago.Archipelago;
import com.thatoneaiguy.archipelago.init.ArchipelagoPackets;
import com.thatoneaiguy.archipelago.packet.CameraModificationS2C;
import com.thatoneaiguy.archipelago.packet.ResetCameraModificationS2C;
import com.thatoneaiguy.archipelago.util.HotbarRenderingUtil;
import nakern.be_camera.camera.CameraData;
import nakern.be_camera.camera.CameraManager;
import nakern.be_camera.camera.EaseOptions;
import nakern.be_camera.easings.Easings;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleRegistry;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;
import team.lodestar.lodestone.systems.particle.render_types.LodestoneWorldParticleRenderType;
import team.lodestar.lodestone.systems.particle.world.type.LodestoneWorldParticleType;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class RiftEntity extends HostileEntity {
    public RiftEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }
    private boolean expanding = false;
    private int expandTicks = 1;
    private float currentRadius = 2.0f;
    private final Set<UUID> collectedPlayers = new HashSet<>();

    private boolean collected = false;

    private static int humSoundCooldown = 0;

    @Override
    public void tick() {
        if (this.getWorld().isClient) createLodestoneParticle(Archipelago.DOT, this.getPos(), (ClientWorld) this.getWorld(), 200, .1f);

        checkAndDiscardIfNearby();
        if (!this.getWorld().isClient) {
            PlayerEntity player = this.getWorld().getClosestPlayer(this, 2);
            if (player != null) {
                Vec3d target = findValidCameraTarget(player, this.getWorld());
                if (this.getBoundingBox().intersects(player.getBoundingBox()) && player.isAlive() && target != null) {
                    CameraModificationS2C packet = new CameraModificationS2C(target, player.getPos(), 1500);
                    ArchipelagoPackets.CHANNEL.sendToClient(packet, (ServerPlayerEntity) player);
                    collected = true;
                } else {
                    ResetCameraModificationS2C packet = new ResetCameraModificationS2C();
                    ArchipelagoPackets.CHANNEL.sendToClient(packet, (ServerPlayerEntity) player);
                }
            }
        }
        spawnFissureParticles(getWorld(), getPos());
    }

    public void checkAndDiscardIfNearby() {
        if (this.getWorld() == null || this.getWorld().isClient) {
            return;
        }

        List<RiftEntity> nearbyEntities = this.getWorld().getEntitiesByClass(
                RiftEntity.class,
                this.getBoundingBox().expand(100.0D),
                other -> other != this
        );

        if (!nearbyEntities.isEmpty()) {
            this.discard();
        }
    }

    public static void createLodestoneParticle(LodestoneWorldParticleType particle, Vec3d globalParticlePos, ClientWorld world, int lifetime, float scale) {
        java.util.Random random = new java.util.Random();

        Color lightPurple = new Color(237,142, 249, 255);
        Color darkPurple = new Color(102, 56, 128);

        int particlesPerTick = 1;

        for (int i = 0; i < particlesPerTick; i++) {
            float speed = 0.01f + random.nextFloat() * 0.03f;

            float theta = (float)(random.nextDouble() * 2 * Math.PI);
            float phi = (float)(Math.acos(2 * random.nextDouble() - 1));

            float dx = speed * MathHelper.sin(phi) * MathHelper.cos(theta);
            float dy = speed * MathHelper.cos(phi);
            float dz = speed * MathHelper.sin(phi) * MathHelper.sin(theta);

            dx += random.nextFloat(-0.01f, 0.01f);
            dy += random.nextFloat(-0.01f, 0.01f);
            dz += random.nextFloat(-0.01f, 0.01f);

            WorldParticleBuilder.create(particle)
                    .enableForcedSpawn()
                    .setLightLevel(LightmapTextureManager.MAX_LIGHT_COORDINATE)
                    .setScaleData(GenericParticleData.create(scale).build())
                    .setTransparencyData(
                            GenericParticleData.create(0.5f, 0f)
                                    .setEasing(Easing.CIRC_IN)
                                    .build()
                    )
                    .setColorData(
                            ColorParticleData.create(lightPurple, darkPurple)
                                    .setEasing(Easing.CIRC_IN)
                                    .build()
                    )
                    .enableNoClip()
                    .enableCull()
                    .setLifetime(lifetime)
                    .setSpinData(SpinParticleData.create(0f, 0.5f).build())
                    .setRenderType(LodestoneWorldParticleRenderType.TRANSPARENT)
                    .setRandomOffset(0.02f)
                    .setMotion(dx, dy, dz)
                    .spawn(
                            world,
                            globalParticlePos.getX(),
                            globalParticlePos.getY(),
                            globalParticlePos.getZ()
                    );
        }
    }

    public static @Nullable Vec3d findValidCameraTarget(PlayerEntity user, World world) {
        if (user != null) {
            Vec3d eyePos = user.getEyePos();
            Vec3d lookVec = user.getRotationVector();

            double maxDistance = 10.0;
            double minDistance = 3.0;
            double stepDistance = 2.0;
            double upOffset = 2.5;
            int maxAttempts = 5;

            for (double distance = maxDistance; distance >= minDistance; distance -= stepDistance) {
                for (int i = 0; i < maxAttempts; i++) {
                    double yawOffset = (i - maxAttempts / 2) * 5.0;
                    Vec3d rotatedVec = lookVec.rotateY((float) Math.toRadians(yawOffset));

                    Vec3d target = eyePos.add(rotatedVec.multiply(distance)).add(0, upOffset, 0);

                    BlockHitResult hit = world.raycast(new RaycastContext(
                            eyePos,
                            target,
                            RaycastContext.ShapeType.OUTLINE,
                            RaycastContext.FluidHandling.NONE,
                            user
                    ));

                    if (hit.getType() == HitResult.Type.MISS) {
                        return target;
                    }
                }
            }
        }

        return null;
    }

    public void triggerCameraModification(PlayerEntity user, World world) {
        HotbarRenderingUtil.setHotbarRendered(false);

        Vec3d eyePos = user.getEyePos();
        Vec3d lookVec = user.getRotationVector();

        double maxDistance = 10.0;
        double minDistance = 3.0;
        double stepDistance = 2.0;
        double upOffset = 2.5;
        int maxAttempts = 5;
        boolean found = false;

        for (double distance = maxDistance; distance >= minDistance; distance -= stepDistance) {
            for (int i = 0; i < maxAttempts; i++) {
                double yawOffset = (i - maxAttempts / 2) * 5.0;
                Vec3d rotatedVec = lookVec.rotateY((float) Math.toRadians(yawOffset));

                Vec3d target = eyePos.add(rotatedVec.multiply(distance)).add(0, upOffset, 0);

                BlockHitResult hit = world.raycast(new RaycastContext(
                        eyePos,
                        target,
                        RaycastContext.ShapeType.OUTLINE,
                        RaycastContext.FluidHandling.NONE,
                        user
                ));

                if (hit.getType() == HitResult.Type.MISS) {
                    if (world.isClient) {
                        CameraManager.INSTANCE.setCamera(
                                new CameraData(
                                        target,
                                        user.getPos(),
                                        new EaseOptions(
                                                Easings.INSTANCE::easeInOutSine,
                                                1500
                                        )
                                )
                        );
                    }
                    found = true;
                    break;
                }
            }
            if (found) break;
        }
    }

    public static void spawnFissureParticles(World world, Vec3d position) {
        if (world.isClient) {
            if (humSoundCooldown-- <= 0) {
                world.playSound(position.x, position.y, position.z,
                        SoundEvents.BLOCK_CONDUIT_AMBIENT,
                        SoundCategory.AMBIENT,
                        0.6f,
                        1.0f + (world.getRandom().nextFloat() - 0.5f) * 0.2f,
                        false
                );
                humSoundCooldown = 100 + world.getRandom().nextInt(40);
            }

            // WISP core - dark aura
            WorldParticleBuilder.create(LodestoneParticleRegistry.WISP_PARTICLE)
                    .setTransparencyData(GenericParticleData.create(0.2f, 0f).setEasing(Easing.SINE_IN).build())
                    .setScaleData(GenericParticleData.create(1.2f + world.getRandom().nextFloat() * 0.4f, 0f).setEasing(Easing.EXPO_OUT).build())
                    .setColorData(ColorParticleData.create(new Color(15, 15, 25), new Color(0, 0, 0))
                            .setCoefficient(1.5f).build())
                    .setLifetime(20 + world.getRandom().nextInt(10))
                    .enableNoClip()
                    .spawn(world, position.x + randomOffset(world), position.y + randomOffset(world, 0.2), position.z + randomOffset(world));

            // STAR glow highlights
            WorldParticleBuilder.create(LodestoneParticleRegistry.STAR_PARTICLE)
                    .setTransparencyData(GenericParticleData.create(0.4f, 0f).setEasing(Easing.CUBIC_IN).build())
                    .setScaleData(GenericParticleData.create(0.4f + world.getRandom().nextFloat() * 0.1f, 0f).setEasing(Easing.SINE_IN).build())
                    .setSpinData(SpinParticleData.create(0.05f, 0.1f)
                            .setEasing(Easing.QUARTIC_IN_OUT).build())
                    .setColorData(ColorParticleData.create(Color.WHITE, new Color(180, 50, 255))
                            .setCoefficient(2.0f).build())
                    .setLifetime(15 + world.getRandom().nextInt(10))
                    .enableNoClip()
                    .spawn(world, position.x + randomOffset(world, 0.1), position.y + 0.1 + randomOffset(world, 0.05), position.z + randomOffset(world, 0.1));
        }
    }

    private static double randomOffset(World world) {
        return (world.getRandom().nextDouble() - 0.5) * 0.6;
    }

    private static double randomOffset(World world, double scale) {
        return (world.getRandom().nextDouble() - 0.5) * scale;
    }
}
