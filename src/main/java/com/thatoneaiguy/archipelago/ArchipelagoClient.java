package com.thatoneaiguy.archipelago;

import com.thatoneaiguy.archipelago.client.ConnectingPurpleLightningRenderer;
import com.thatoneaiguy.archipelago.init.ArchipelagoBlocks;
import com.thatoneaiguy.archipelago.init.ArchipelagoEntities;
import com.thatoneaiguy.archipelago.init.ArchipelagoPackets;
import com.thatoneaiguy.archipelago.init.ArchipelagoParticles;
import com.thatoneaiguy.archipelago.packet.VaseBreakPacket;
import com.thatoneaiguy.archipelago.render.entity.RiftRenderer;
import com.thatoneaiguy.archipelago.render.entity.abilities.runic.TotemEntityModel;
import com.thatoneaiguy.archipelago.render.entity.abilities.runic.TotemEntityRenderer;
import com.thatoneaiguy.archipelago.render.entity.abilities.runic.acolyte.AcolyteBloodEntityRenderer;
import com.thatoneaiguy.archipelago.util.runic.magic.RelikInputHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import team.lodestar.lodestone.systems.particle.world.type.LodestoneWorldParticleType;

import static com.thatoneaiguy.archipelago.Archipelago.DOT;

public class ArchipelagoClient implements ClientModInitializer {
    public static final EntityModelLayer MODEL_TEST_LAYER = new EntityModelLayer(new Identifier("archipelago", "test"), "main");

    private static int flashTimer = 0;

    @Override
    public void onInitializeClient() {
        MinecraftClient client = MinecraftClient.getInstance();

        //ParticleFactoryRegistry.getInstance().register(ArchipelagoParticles.VASE_BREAK_PARTICLE, VaseBreakParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ArchipelagoParticles.BLOOD, FlameParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(DOT, LodestoneWorldParticleType.Factory::new);

        registerRenderLayerMaps();
        registerClientNetworking();
        registerEntityRenderers();

       WorldRenderEvents.AFTER_ENTITIES.register(context -> {
            if (client.world == null || client.player == null) return;

            MatrixStack matrices = context.matrixStack();
            Vec3d cameraPos = context.camera().getPos();
            Random random = client.world.getRandom();

            for (Entity entity : client.world.getEntities()) {
                if (entity.getType().equals(ArchipelagoEntities.RIFT_ENTITY_TYPE)) {
                    Vec3d riftPos = entity.getLerpedPos(context.tickDelta()).add(0, (entity.getHeight() / 2) - 1, 0);
                    ConnectingPurpleLightningRenderer.tick(random, riftPos); // update logic
                    ConnectingPurpleLightningRenderer.render(matrices, context.consumers(), cameraPos, riftPos);
                }
            }
        });

        ArchipelagoPackets.CHANNEL.initClientListener();
        ArchipelagoPackets.registerC2S();

        new RelikInputHandler();
    }

    private void registerEntityRenderers() {
        EntityRendererRegistry.register(ArchipelagoEntities.TOTEM_ENTITY_TYPE, TotemEntityRenderer::new);
        EntityRendererRegistry.register(ArchipelagoEntities.BLOOD_ENTITY_TYPE, AcolyteBloodEntityRenderer::new);
        EntityRendererRegistry.register(ArchipelagoEntities.RIFT_ENTITY_TYPE, RiftRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(MODEL_TEST_LAYER, TotemEntityModel::getTexturedModelData);
    }

    private void registerClientNetworking() {
        ClientPlayNetworking.registerGlobalReceiver(VaseBreakPacket.ID, ((client, handler, buf, responseSender) -> {
            Vec3d position = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
            client.execute(() -> {
                ClientPlayerEntity player = client.player;
                if (player != null) {
                    client.player.getWorld().addParticle(ArchipelagoParticles.VASE_BREAK_PARTICLE, position.getX(), position.getY(), position.getZ(), 0.0, 0.0, 0.0);
                    double distance = player.getPos().distanceTo(position);
                }

            });
        }));
    }

    private void registerRenderLayerMaps() {
        BlockRenderLayerMap.INSTANCE.putBlock(ArchipelagoBlocks.RESENTMENT_SINK, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(ArchipelagoBlocks.CRYSTAL_LEAVES, RenderLayer.getCutout());
        //BlockRenderLayerMap.INSTANCE.putBlock(ArchipelagoBlocks.CRYSTAL_SAPLING, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(ArchipelagoBlocks.PRIVACY_GLASS, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ArchipelagoBlocks.PRIVACY_GLASS_PANEL, RenderLayer.getTranslucent());
    }
}
