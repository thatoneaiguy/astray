package com.thatoneaiguy.archipelago.render.entity.abilities.runic.acolyte;

import com.thatoneaiguy.archipelago.ArchipelagoClient;
import com.thatoneaiguy.archipelago.entity.runic.TotemEntity;
import com.thatoneaiguy.archipelago.entity.runic.acolyte.AcolyteBloodEntity;
import com.thatoneaiguy.archipelago.render.entity.abilities.runic.TotemEntityModel;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class AcolyteBloodEntityRenderer extends MobEntityRenderer<AcolyteBloodEntity, AcolyteBloodEntityModel> {

    public AcolyteBloodEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new AcolyteBloodEntityModel(context.getPart(ArchipelagoClient.MODEL_TEST_LAYER)), 0.5f);
    }

    @Override
    public Identifier getTexture(AcolyteBloodEntity entity) {
        return new Identifier("archipelago", "textures/entity/nope.png");
    }
}