package com.thatoneaiguy.archipelago.mixin;

import com.thatoneaiguy.archipelago.entity.runic.acolyte.AcolyteBloodEntity;
import com.thatoneaiguy.archipelago.render.systems.TrailPoint;
import com.thatoneaiguy.archipelago.render.systems.VFXBuilders;
import com.thatoneaiguy.archipelago.util.PositionTrackedEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.List;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> {
    @Shadow protected abstract void initDataTracker();

    private static final RenderLayer TRAIL_TYPE = LodestoneRenderTypeRegistry.ADDITIVE_TEXTURE_TRIANGLE.apply(RenderTypeToken.createCachedToken(Effective.id("textures/vfx/light_trail.png")));

    protected LivingEntityMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    public RenderLayer getTrailRenderType() {
        return TRAIL_TYPE;
    }

    @Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("TAIL"))
    public void render(T livingEntity, float entityYaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, CallbackInfo ci) {
        if (livingEntity instanceof AcolyteBloodEntity blood) {
            matrixStack.push();
            List<TrailPoint> positions = ((PositionTrackedEntity) blood).getPastPositions();
            VFXBuilders.WorldVFXBuilder builder = VFXBuilders.createWorld().setRenderType(getTrailRenderType());

            float size = 0.2f;
            float alpha = 1f;

            float x = (float) MathHelper.lerp(tickDelta, blood.prevX, blood.getX());
            float y = (float) MathHelper.lerp(tickDelta, blood.prevY, blood.getY());
            float z = (float) MathHelper.lerp(tickDelta, blood.prevZ, blood.getZ());

            matrixStack.translate(-x, -y, -z);
            builder.setColor(new Color(0x241210))
                    .setAlpha(alpha)
                    .renderTrail(matrixStack,
                            positions,
                            f -> MathHelper.sqrt(f) * size,
                            f -> builder.setAlpha((float) Math.cbrt(Math.max(0, (alpha * f) - 0.1f)))
                    )
                    .setAlpha(alpha)
                    .renderTrail(matrixStack,
                            positions,
                            f -> (MathHelper.sqrt(f) * size) / 1.5f,
                            f -> builder.setAlpha((float) Math.cbrt(Math.max(0, (((alpha * f) / 1.5f) - 0.1f))))
                    );

            matrixStack.pop();
        }
    }
}