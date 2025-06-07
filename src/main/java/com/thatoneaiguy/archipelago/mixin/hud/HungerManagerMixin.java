package com.thatoneaiguy.archipelago.mixin.hud;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.thatoneaiguy.archipelago.Archipelago;
import com.thatoneaiguy.archipelago.util.LocationHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HungerManager.class)
public class HungerManagerMixin {
    /*@Shadow
    private int foodLevel = 20;
    @Unique
    MinecraftClient client;

    *//**
     * @author eeverest
     * @reason constant sprinting, this prevents loosing sprint
     *//*
    @Overwrite
    public void setFoodLevel(int foodLevel) {
        if (!LocationHelper.isPlayerInArchipelago(client.player)) {
            this.foodLevel = 20;
        } else {
            this.foodLevel = foodLevel;
        }
    }

    @Inject(method = "update(Lnet/minecraft/entity/player/PlayerEntity;)V", at = @At(value = "INVOKE"
            , target = "net/minecraft/entity/player/PlayerEntity.damage (Lnet/minecraft/entity/damage/DamageSource;F)Z"), cancellable = true)
    private void archipelago$removeHungerDamage(PlayerEntity player, CallbackInfo ci) {
        ci.cancel();
    }*/
}
