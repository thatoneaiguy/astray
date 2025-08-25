package com.thatoneaiguy.archipelago.render.entity.abilities.runic;

import com.thatoneaiguy.archipelago.Archipelago;
import com.thatoneaiguy.archipelago.ArchipelagoClient;
import com.thatoneaiguy.archipelago.entity.runic.TotemEntity;
import com.thatoneaiguy.archipelago.init.ArchipelagoItems;
import com.thatoneaiguy.archipelago.mixin.access.ItemRendererAccessor;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.TridentEntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
    }
}
