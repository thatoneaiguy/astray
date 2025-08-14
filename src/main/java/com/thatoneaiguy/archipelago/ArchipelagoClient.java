package com.thatoneaiguy.archipelago;

import com.thatoneaiguy.archipelago.client.ConnectingPurpleLightningRenderer;
import com.thatoneaiguy.archipelago.init.ArchipelagoBlocks;
import com.thatoneaiguy.archipelago.init.ArchipelagoEntities;
import com.thatoneaiguy.archipelago.init.ArchipelagoPackets;
import com.thatoneaiguy.archipelago.init.ArchipelagoParticles;
import com.thatoneaiguy.archipelago.packet.VaseBreakPacket;
import com.thatoneaiguy.archipelago.particle.OutwardRiftParticle;
import com.thatoneaiguy.archipelago.render.entity.RiftRenderer;
import com.thatoneaiguy.archipelago.render.entity.abilities.runic.TotemEntityModel;
import com.thatoneaiguy.archipelago.render.entity.abilities.runic.TotemEntityRenderer;
import com.thatoneaiguy.archipelago.render.entity.abilities.runic.acolyte.AcolyteBloodEntityRenderer;
import com.thatoneaiguy.archipelago.util.runic.magic.RelikInputHandler;
import foundry.veil.api.client.color.Color;
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

public class ArchipelagoClient implements ClientModInitializer {
    public static final EntityModelLayer MODEL_TEST_LAYER = new EntityModelLayer(new Identifier("archipelago", "test"), "main");


    @Override
    public void onInitializeClient() {
        MinecraftClient client = MinecraftClient.getInstance();

        // Shaders
        //makeRiftShaders();

        //ParticleFactoryRegistry.getInstance().register(ArchipelagoParticles.VASE_BREAK_PARTICLE, VaseBreakParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ArchipelagoParticles.BLOOD, FlameParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ArchipelagoParticles.OUTWARD_RIFT, OutwardRiftParticle.Factory::new);
        //ParticleFactoryRegistry.getInstance().register(DOT, LodestoneWorldParticleType.Factory::new);

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


    // ! Shaders!!!!

    public static Color getClientColor() {
        try {
            return Color.WHITE;
        }catch (Exception e){
            return new Color(255, 255, 255);
        }
    }


    /*private void makeRiftShaders() {
        World level = MinecraftClient.getInstance().world;
        if (level == null || MinecraftClient.getInstance().player == null) return;
        BlockPos pos = MinecraftClient.getInstance().player.getBlockPos();
        for (RiftEntity beacon : level.getEntitiesByClass(RiftEntity.class, new Box(pos.getX() + 120, pos.getY() + 80, pos.getZ() + 120, pos.getX() - 120, pos.getY() - 80, pos.getZ() - 120), null)) {

            beacon.addTimer(MinecraftClient.getInstance().getTickDelta());
            riftShaderActivator(beacon.getPos(), beacon.getTimer());
        }
    }*/

    /*public static void activateRiftGlowShader(Vec3d pos, float radius, float glowWidth, float timer) {
        try {
            PostProcessingManager ppm = VeilRenderSystem.renderer().getPostProcessingManager();
            PostPipeline pipeline = ppm.getPipeline(new Identifier(MODID, "rift"));
            if (pipeline == null) {
                return;
            }

            // Set uniforms
            pipeline.setVector("pos", (float) pos.x, (float) pos.y, (float) pos.z);
            pipeline.setFloat("radius", radius);
            pipeline.setFloat("glowWidth", glowWidth);
            pipeline.setFloat("timer", timer);

            ppm.runPipeline(pipeline);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    /*private Matrix4f getViewMatrix(MinecraftClient client, float tickDelta) {
        Camera camera = client.gameRenderer.getCamera();
        Vec3d pos = camera.getPos();

        Matrix4f rotation = camera.getRotation().get(new Matrix4f());
        rotation.invert();

        Matrix4f translation = new Matrix4f().translation((float) -pos.x, (float) -pos.y, (float) -pos.z);

        rotation.mul(translation);

        return rotation; // full view matrix
    }

    private Matrix4f getProjectionMatrix(MinecraftClient client, float tickDelta) {
        float fov = (float) Math.toRadians(client.options.getFov().getValue());
        float aspectRatio = (float) client.getWindow().getFramebufferWidth() / client.getWindow().getFramebufferHeight();
        float nearPlane = 0.05f;
        int viewDistance = MinecraftClient.getInstance().options.getViewDistance().getValue();
        float farPlane = viewDistance * 16f;

        Matrix4f projection = new Matrix4f().perspective(fov, aspectRatio, nearPlane, farPlane);
        return projection;
    }


    private void riftShaderActivator(Vec3d pos, float timer) {
        MinecraftClient client = MinecraftClient.getInstance();
        PostProcessingManager ppm = VeilRenderSystem.renderer().getPostProcessingManager();
        PostPipeline pipeline = ppm.getPipeline(Identifier.of("archipelago", "rift"));
        if (pipeline == null || client.world == null) return;

        float tickDelta = client.getTickDelta();
        Camera camera = client.gameRenderer.getCamera();

        Matrix4f viewMatrix = getViewMatrix(client, tickDelta); // your method
        Matrix4f projMatrix = getProjectionMatrix(client, tickDelta); // your method

        Matrix4f invViewMatrix = new Matrix4f(viewMatrix).invert();
        Matrix4f invProjMatrix = new Matrix4f(projMatrix).invert();

        Vec3d cameraPos = camera.getPos();

        int viewDistance = client.options.getViewDistance().getValue();
        float nearPlane = 0.05f;
        float farPlane = viewDistance * 16f;

        pipeline.setMatrix("invViewMat", invViewMatrix);
        pipeline.setMatrix("invProjMat", invProjMatrix);

        pipeline.setVector("cameraPos", (float) cameraPos.x, (float) cameraPos.y, (float) cameraPos.z);
        pipeline.setFloat("nearPlane", nearPlane);
        pipeline.setFloat("farPlane", farPlane);

        pipeline.setVector("pos", (float) pos.x, (float) pos.y, (float) pos.z);
        pipeline.setFloat("radius", 10f);       // Adjust radius as needed
        pipeline.setFloat("glowWidth", 2f);     // Use if shader uses glowWidth
        pipeline.setFloat("timer", timer);
        pipeline.setFloat("GameTime", client.world.getTime() + tickDelta);

        ppm.runPipeline(pipeline);
    }*/
}
