package com.thatoneaiguy.archipelago.render.entity.abilities.runic;

import com.thatoneaiguy.archipelago.ArchipelagoClient;
import com.thatoneaiguy.archipelago.entity.runic.TotemEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class TotemEntityRenderer extends MobEntityRenderer<TotemEntity, TotemEntityModel> {

    public TotemEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new TotemEntityModel(context.getPart(ArchipelagoClient.MODEL_TEST_LAYER)), 0.5f);
    }

    @Override
    public Identifier getTexture(TotemEntity entity) {
        return new Identifier("archipelago", "textures/entity/cube/cube.png");
    }
}
