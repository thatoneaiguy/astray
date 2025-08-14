package com.thatoneaiguy.archipelago.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class OutwardRiftParticle extends SpriteBillboardParticle {

    private final float initialScale;
    private final int maxAge;
    private float spinSpeed;
    private float spinAngle;

    protected OutwardRiftParticle(ClientWorld world, double x, double y, double z,
                                  double velocityX, double velocityY, double velocityZ,
                                  float scale, int maxAge) {
        super(world, x, y, z, velocityX, velocityY, velocityZ);
        this.initialScale = scale;
        this.maxAge = maxAge;

        this.scale = scale / 2;

        this.setAlpha(0.5f);
        this.red = 237f / 255f;
        this.green = 142f / 255f;
        this.blue = 249f / 255f;

        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;

        this.spinSpeed = 0.5f * (world.random.nextBoolean() ? 1 : -1); // random spin direction
        this.spinAngle = 0f;

        this.collidesWithWorld = false;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.age >= this.maxAge) {
            this.markDead();
            return;
        }

        spinAngle += spinSpeed;

        float lifeProgress = (float) this.age / (float) this.maxAge;
        this.alpha = MathHelper.clamp(0.5f * (1.0f - lifeProgress), 0.0f, 0.5f);

        this.scale = initialScale * (1.0f - lifeProgress);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(DefaultParticleType type, ClientWorld world,
                                       double x, double y, double z,
                                       double velocityX, double velocityY, double velocityZ) {
            OutwardRiftParticle particle = new OutwardRiftParticle(world, x, y, z, velocityX, velocityY, velocityZ,
                    0.3f + world.random.nextFloat() * 0.5f,
                    30 + world.random.nextInt(20));
            particle.setSprite(this.spriteProvider);
            return particle;
        }
    }
}

