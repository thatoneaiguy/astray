package com.thatoneaiguy.archipelago.render.entity.abilities.runic.acolyte;

import com.thatoneaiguy.archipelago.entity.runic.acolyte.AcolyteBloodEntity;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class AcolyteBloodEntityModel extends EntityModel<AcolyteBloodEntity> {

    public AcolyteBloodEntityModel(ModelPart root) {}

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        return TexturedModelData.of(modelData, 16, 16);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {}

    @Override
    public void setAngles(AcolyteBloodEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {}
}
