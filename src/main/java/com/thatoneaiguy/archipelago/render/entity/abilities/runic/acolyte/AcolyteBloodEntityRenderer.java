package com.thatoneaiguy.archipelago.render.entity.abilities.runic.acolyte;

import com.thatoneaiguy.archipelago.Archipelago;
import com.thatoneaiguy.archipelago.ArchipelagoClient;
import com.thatoneaiguy.archipelago.entity.runic.acolyte.AcolyteBloodEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.awt.*;

public class AcolyteBloodEntityRenderer extends MobEntityRenderer<AcolyteBloodEntity, AcolyteBloodEntityModel> {

    public AcolyteBloodEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new AcolyteBloodEntityModel(context.getPart(ArchipelagoClient.MODEL_TEST_LAYER)), 0.5f);
    }

    @Override
    public Identifier getTexture(AcolyteBloodEntity entity) {
        return new Identifier("archipelago", "textures/entity/nope.png");
    }

    @Override
    public void render(AcolyteBloodEntity blood, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        super.render(blood, f, g, matrixStack, vertexConsumerProvider, i);
    }
}