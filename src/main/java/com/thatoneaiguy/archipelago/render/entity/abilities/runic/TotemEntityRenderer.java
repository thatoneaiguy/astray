package com.thatoneaiguy.archipelago.render.entity.abilities.runic;

import com.thatoneaiguy.archipelago.Archipelago;
import com.thatoneaiguy.archipelago.ArchipelagoClient;
import com.thatoneaiguy.archipelago.entity.runic.TotemEntity;
import com.thatoneaiguy.archipelago.init.ArchipelagoItems;
import com.thatoneaiguy.archipelago.mixin.access.ItemRendererAccessor;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.TridentEntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.lang3.arch.Processor;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.UUID;

public class TotemEntityRenderer extends MobEntityRenderer<TotemEntity, TotemEntityModel> {
    private final Identifier chainTexture = new Identifier(Archipelago.MODID, "textures/entity/chain.png");
    private final ItemRenderer itemRenderer;


    public TotemEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new TotemEntityModel(context.getPart(ArchipelagoClient.MODEL_TEST_LAYER)), 0.5f);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public Identifier getTexture(TotemEntity entity) {
        return new Identifier("archipelago", "textures/entity/cube/cube.png");
    }

    @Override
    public void render(TotemEntity totem, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        super.render(totem, yaw, tickDelta, matrices, vertexConsumers, light);

        Item item = ArchipelagoItems.JAR;
        matrices.push();
        matrices.translate(0, 10, 0);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));
        BakedModel model = this.itemRenderer.getModel(item.getDefaultStack(), totem.getWorld(), totem, 0);
        RenderLayer renderLayer = RenderLayers.getItemLayer(item.getDefaultStack(), false);
        ((ItemRendererAccessor) this.itemRenderer).observations$renderBakedItemModel(model, item.getDefaultStack(), light, OverlayTexture.DEFAULT_UV, matrices, ItemRenderer.getItemGlintConsumer(vertexConsumers, renderLayer, false, false));
        matrices.pop();
    }

    private void vertex(Vec3d vec, VertexConsumer vertexConsumer, float u, float v, Matrix4f modelMatrix, Matrix3f normal, int light) {
        vertexConsumer.vertex(modelMatrix, (float)vec.x, (float)vec.y, (float)vec.z).color(255, 255, 255, 255).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normal, 0.0F, 1.0F, 0.0F).next();
    }
}
