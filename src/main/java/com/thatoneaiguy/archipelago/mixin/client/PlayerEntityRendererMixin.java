package com.thatoneaiguy.archipelago.mixin.client;

import com.thatoneaiguy.archipelago.Archipelago;
import com.thatoneaiguy.archipelago.init.ArchipelagoItems;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {
    @Inject(
            method = "getArmPose(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/client/render/entity/model/BipedEntityModel$ArmPose;",
            at = @At("TAIL"),
            cancellable = true
    )
    private static void HoldingPos(AbstractClientPlayerEntity player, Hand hand, CallbackInfoReturnable<BipedEntityModel.ArmPose> ci) {
        ItemStack mainHand = player.getStackInHand(hand);
        if (mainHand.getItem() == ArchipelagoItems.ASTROLOGICAL_GREATSWORD) {
            ci.setReturnValue(BipedEntityModel.ArmPose.CROSSBOW_HOLD);
            ci.cancel();
        }
    }
}