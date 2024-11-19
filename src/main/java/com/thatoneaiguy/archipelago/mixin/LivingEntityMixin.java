/*
package com.thatoneaiguy.archipelago.mixin;

import com.thatoneaiguy.archipelago.Archipelago;
import com.thatoneaiguy.archipelago.entity.runic.acolyte.AcolyteBloodEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.ArrayList;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> {
    private static final Identifier LIGHT_TRAIL = new Identifier(Archipelago.MODID, "textures/vfx/light_trail.png");
    private static final RenderLayer LIGHT_TYPE = LodestoneRenderLayers.ADDITIVE_TEXTURE.apply(LIGHT_TRAIL);

    protected LivingEntityMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Inject(method = "render(Lnet/minecraft/entity/Entity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("TAIL"))
    public void render(Entity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (entity instanceof AcolyteBloodEntity blood) {
            matrices.push();
            ArrayList<Vec3d> positions = new ArrayList<>(((PositionTrackedEntity) blood).getPastPositions());
            VFXBuilders.WorldVFXBuilder builder = VFXBuilders.createWorld().setPosColorTexLightmapDefaultFormat();

            float size = 0.2f;
            float alpha = 1f;

            float x = (float) MathHelper.lerp(tickDelta, blood.prevX, blood.getX());
            float y = (float) MathHelper.lerp(tickDelta, blood.prevY, blood.getY());
            float z = (float) MathHelper.lerp(tickDelta, blood.prevZ, blood.getZ());

            builder.setColor(new Color(data.color)).setOffset(-x, -y, -z)
                    .setAlpha(alpha)
                    .renderTrail(
                            DELAYED_RENDER.getBuffer(LIGHT_TYPE),
                            matrices,
                            positions.stream()
                                    .map(p -> new Vector4f((float) p.x, (float) p.y, (float) p.z, 1))
                                    .toList(),
                            f -> MathHelper.sqrt(f) * size,
                            f -> builder.setAlpha((float) Math.cbrt(Math.max(0, (alpha * f) - 0.1f)))
                    )
                    .renderTrail(
                            DELAYED_RENDER.getBuffer(LIGHT_TYPE),
                            matrices,
                            positions.stream()
                                    .map(p -> new Vector4f((float) p.x, (float) p.y, (float) p.z, 1))
                                    .toList(),
                            f -> (MathHelper.sqrt(f) * size) / 1.5f,
                            f -> builder.setAlpha((float) Math.cbrt(Math.max(0, (((alpha * f) / 1.5f) - 0.1f))))
                    );

            matrixStack.pop();
        }
    }
}*/
