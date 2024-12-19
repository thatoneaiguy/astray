package com.thatoneaiguy.archipelago.mixin.vfx;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.thatoneaiguy.archipelago.util.LocationHelper;
import net.minecraft.client.particle.CloudParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CloudParticle.SneezeFactory.class)
public class SneezeMixin {
    @WrapMethod(
            method = "createParticle(Lnet/minecraft/particle/DefaultParticleType;Lnet/minecraft/client/world/ClientWorld;DDDDDD)Lnet/minecraft/client/particle/Particle;"
    )
    private Particle archipelago$removeClouds(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i, Operation<Particle> original) {
        if (!LocationHelper.isPlayerInArchipelagoFromWorld(clientWorld)) {
            original.call(defaultParticleType, clientWorld, d, e, f, g, h, i);
        }
        return null;
    }
}
