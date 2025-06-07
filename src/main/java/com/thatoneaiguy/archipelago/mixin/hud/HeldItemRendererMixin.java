package com.thatoneaiguy.archipelago.mixin.hud;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.thatoneaiguy.archipelago.util.HotbarRenderingUtil;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {
    @WrapMethod(
            method = "renderArm"
    )
    public void archipelago$stopRenderingArm(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, Arm arm, Operation<Void> original) {
        if (HotbarRenderingUtil.isHotbarRendered()) {
            original.call(matrices, vertexConsumers, light, arm);
        }
    }

    @WrapMethod(
            method = "renderFirstPersonItem"
    )
    public void archipelago$stopRenderingArm(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, Operation<Void> original) {
        if (HotbarRenderingUtil.isHotbarRendered()) {
            original.call(player, tickDelta, pitch, hand, swingProgress, item, equipProgress, matrices, vertexConsumers, light);
        }
    }
}
