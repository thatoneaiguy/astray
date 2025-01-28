package com.thatoneaiguy.archipelago.entity.runic.acolyte;

import com.thatoneaiguy.archipelago.entity.ArchipelagoEntity;
import com.thatoneaiguy.archipelago.init.ArchipelagoParticles;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.VexEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.raid.RaiderEntity;
    import net.minecraft.world.World;

public class AcolyteBloodEntity extends VexEntity {
    public AcolyteBloodEntity(EntityType<? extends VexEntity> entityType, World world) {
        super(entityType, world);
    }
    /*protected static final TrackedData<Byte> VEX_FLAGS = DataTracker.registerData(AcolyteBloodEntity.class, TrackedDataHandlerRegistry.BYTE);
    private static final int CHARGING_FLAG = 1;*/

   /* public AcolyteBloodEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }
*/
    public static DefaultAttributeContainer.Builder setAttributes() {
        return HostileEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 1.0)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1.0)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 1.0);
    }

    @Override
    public void tick() {
        this.getWorld().addParticle(ArchipelagoParticles.BLOOD, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);

        super.tick();
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(4, new ChargeTargetGoal());
        this.goalSelector.add(8, new LookAtTargetGoal());
        this.goalSelector.add(9, new LookAtEntityGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.add(10, new LookAtEntityGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.add(1, (new RevengeGoal(this, new Class[]{RaiderEntity.class})).setGroupRevenge(new Class[0]));
        this.targetSelector.add(2, new ActiveTargetGoal(this, ArchipelagoEntity.class, true));
    }

    /*@Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(4, new ChargeTargetGoal());
        this.goalSelector.add(8, new LookAtTargetGoal());
        this.goalSelector.add(10, new LookAtEntityGoal(this, MobEntity.class, 8.0f));
        this.targetSelector.add(1, new RevengeGoal(this).setGroupRevenge(new Class[0]));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, HostileEntity.class, true));
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(VEX_FLAGS, (byte)0);

        super.initDataTracker();
    }

    @Override
    public void tick() {
        this.world.addParticle(ArchipelagoParticles.BLOOD, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);

        super.tick();
    }

    @Override
    public boolean hasWings() {
        return true;
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

    private void setVexFlag(int mask, boolean value) {
        int i = this.dataTracker.get(VEX_FLAGS).byteValue();
        i = value ? (i |= mask) : (i &= ~mask);
        this.dataTracker.set(VEX_FLAGS, (byte)(i & 0xFF));
    }

    private boolean areFlagsSet(int mask) {
        byte i = this.dataTracker.get(VEX_FLAGS);
        return (i & mask) != 0;
    }

    public void setCharging(boolean charging) {
        this.setVexFlag(CHARGING_FLAG, charging);
    }

    public boolean isCharging() {
        return this.areFlagsSet(CHARGING_FLAG);
    }

    class ChargeTargetGoal extends Goal {
        public ChargeTargetGoal() {
            this.setControls(EnumSet.of(Goal.Control.MOVE));
        }

        @Override
        public boolean canStart() {
            if (AcolyteBloodEntity.this.getTarget() != null && !AcolyteBloodEntity.this.getMoveControl().isMoving() && AcolyteBloodEntity.this.random.nextInt(ChargeTargetGoal.toGoalTicks(7)) == 0) {
                return AcolyteBloodEntity.this.squaredDistanceTo(AcolyteBloodEntity.this.getTarget()) > 4.0;
            }
            return false;
        }

        @Override
        public boolean shouldContinue() {
            return AcolyteBloodEntity.this.getMoveControl().isMoving() && AcolyteBloodEntity.this.isCharging() && AcolyteBloodEntity.this.getTarget() != null && AcolyteBloodEntity.this.getTarget().isAlive();
        }

        @Override
        public void start() {
            LivingEntity livingEntity = AcolyteBloodEntity.this.getTarget();
            if (livingEntity != null) {
                Vec3d vec3d = livingEntity.getEyePos();
                AcolyteBloodEntity.this.moveControl.moveTo(vec3d.x, vec3d.y, vec3d.z, 1.0);
            }
            AcolyteBloodEntity.this.setCharging(true);
            AcolyteBloodEntity.this.playSound(SoundEvents.ENTITY_VEX_CHARGE, 1.0f, 1.0f);
        }

        @Override
        public void stop() {
            AcolyteBloodEntity.this.setCharging(false);
        }

        @Override
        public boolean shouldRunEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity livingEntity = AcolyteBloodEntity.this.getTarget();
            if (livingEntity == null) {
                return;
            }
            if (AcolyteBloodEntity.this.getBoundingBox().intersects(livingEntity.getBoundingBox())) {
                AcolyteBloodEntity.this.tryAttack(livingEntity);
                AcolyteBloodEntity.this.setCharging(false);
            } else {
                double d = AcolyteBloodEntity.this.squaredDistanceTo(livingEntity);
                if (d < 9.0) {
                    Vec3d vec3d = livingEntity.getEyePos();
                    AcolyteBloodEntity.this.moveControl.moveTo(vec3d.x, vec3d.y, vec3d.z, 1.3);
                }
            }
        }
    }

    class LookAtTargetGoal extends Goal {
        public LookAtTargetGoal() {
            this.setControls(EnumSet.of(Goal.Control.MOVE));
        }

        @Override
        public boolean canStart() {
            return !AcolyteBloodEntity.this.getMoveControl().isMoving() && AcolyteBloodEntity.this.random.nextInt(LookAtTargetGoal.toGoalTicks(7)) == 0;
        }

        @Override
        public boolean shouldContinue() {
            return false;
        }

        @Override
        public void tick() {
            BlockPos blockPos = AcolyteBloodEntity.this.getBlockPos();
            if (blockPos == null) {
                blockPos = AcolyteBloodEntity.this.getBlockPos();
            }
            for (int i = 0; i < 3; ++i) {
                BlockPos blockPos2 = blockPos.add(AcolyteBloodEntity.this.random.nextInt(15) - 7, AcolyteBloodEntity.this.random.nextInt(11) - 5, AcolyteBloodEntity.this.random.nextInt(15) - 7);
                if (!AcolyteBloodEntity.this.world.isAir(blockPos2)) continue;
                AcolyteBloodEntity.this.moveControl.moveTo((double)blockPos2.getX() + 0.5, (double)blockPos2.getY() + 0.5, (double)blockPos2.getZ() + 0.5, 0.25);
                if (AcolyteBloodEntity.this.getTarget() != null) break;
                AcolyteBloodEntity.this.getLookControl().lookAt((double)blockPos2.getX() + 0.5, (double)blockPos2.getY() + 0.5, (double)blockPos2.getZ() + 0.5, 180.0f, 20.0f);
                break;
            }
        }
    }*/
}
