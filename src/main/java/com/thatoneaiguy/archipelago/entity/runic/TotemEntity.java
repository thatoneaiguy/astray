package com.thatoneaiguy.archipelago.entity.runic;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class TotemEntity extends HostileEntity {
    int ticks = 0;
    private ServerPlayerEntity owner;
    private long creationTime;

    public TotemEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    public void setOwner(ServerPlayerEntity owner) {
        this.owner = owner;
        this.creationTime = getWorld().getTime();
    }

    public ServerPlayerEntity getOwner() {
        return this.owner;
    }

    public long getCreationTime() {
        return this.creationTime;
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return HostileEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 14.0);
    }

    @Override
    public void tick() {
        ticks++;

        for (int i = 0; i < 4; i++) {
            Vec3d vec3d = new Vec3d(
                    this.getX() + (MathHelper.sin((-this.age) * 0.75f + i * 45) * this.getWidth() * 1.2),
                    this.getBodyY(0.5f),
                    this.getZ() + (MathHelper.cos((-this.age) * 0.75f + i * 45) * this.getWidth() * 1.2));

            getWorld().addParticle(ParticleTypes.CRIT, vec3d.getX(), vec3d.getY(), vec3d.getZ(), 0.0, 0.0, 0.0);
        }

        if ( ticks >= 1800 ) {
            this.discard();
            ticks = 0;
        }

        super.tick();
    }


    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    @Override
    public void pushAwayFrom(Entity entity) {
        // Override to prevent this entity from colliding with others
    }

    @Override
    public boolean isPushable() {
        return false; // Prevents this entity from being pushed by others
    }

    @Override
    protected void tickCramming() {
        // Override to do nothing, disables entity cramming effects
    }
}
