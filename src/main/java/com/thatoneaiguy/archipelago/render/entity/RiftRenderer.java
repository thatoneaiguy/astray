package com.thatoneaiguy.archipelago.render.entity;

import com.thatoneaiguy.archipelago.ArchipelagoClient;
import com.thatoneaiguy.archipelago.entity.RiftEntity;
import com.thatoneaiguy.archipelago.entity.runic.acolyte.AcolyteBloodEntity;
import com.thatoneaiguy.archipelago.render.entity.abilities.runic.acolyte.AcolyteBloodEntityModel;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

public class RiftRenderer extends MobEntityRenderer<RiftEntity, RiftEntityModel> {
    public RiftRenderer(EntityRendererFactory.Context context) {
        super(context, new RiftEntityModel(context.getPart(ArchipelagoClient.MODEL_TEST_LAYER)), 0.5f);
    }

    @Override
    public Identifier getTexture(RiftEntity entity) {
        return new Identifier("archipelago", "textures/entity/nope.png");
    }

    @Override
    public void render(RiftEntity rift, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        super.render(rift, f, g, matrixStack, vertexConsumerProvider, i);
    }
}
