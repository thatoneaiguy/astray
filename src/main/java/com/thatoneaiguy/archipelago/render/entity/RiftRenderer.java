package com.thatoneaiguy.archipelago.render.entity;

import com.thatoneaiguy.archipelago.ArchipelagoClient;
import com.thatoneaiguy.archipelago.entity.RiftEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

import java.util.*;

public class RiftRenderer extends MobEntityRenderer<RiftEntity, RiftEntityModel> {
    private static final int MAX_ARCS = 5;
    private static final int ARC_SEGMENTS = 3;
    private static final double ARC_RANGE = 3.0;

    private final Map<Integer, List<LightningArc>> arcMap = new HashMap<>();

    public RiftRenderer(EntityRendererFactory.Context context) {
        super(context, new RiftEntityModel(context.getPart(ArchipelagoClient.MODEL_TEST_LAYER)), 0.5f);
    }

    @Override
    public Identifier getTexture(RiftEntity entity) {
        return new Identifier("archipelago", "textures/entity/nope.png");
    }

    @Override
    public void render(RiftEntity rift, float yaw, float partialTicks, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light) {
        super.render(rift, yaw, partialTicks, matrices, vertexConsumers, light);

        /*int entityUUID = rift.getId();
        Vec3d origin = rift.getPos();

        List<LightningArc> arcs = arcMap.computeIfAbsent(entityUUID, k -> {
            List<LightningArc> newArcs = new ArrayList<>();
            for (int idx = 0; idx < MAX_ARCS; idx++) {
                newArcs.add(LightningArc.createRandomArc(origin));
            }
            return newArcs;
        });

        Vec3d cameraPos = MinecraftClient.getInstance().gameRenderer.getCamera().getPos();

        VertexConsumer buffer = vertexConsumers.getBuffer(RenderLayer.getLines());

        matrices.push();
        Matrix4f matrix = matrices.peek().getPositionMatrix();

        for (LightningArc arc : arcs) {
            arc.age++;
            for (int idx = 1; idx < arc.segments.size(); idx++) {
                Vec3d from = arc.segments.get(idx - 1);
                Vec3d to = arc.segments.get(idx);
                drawTriangularBolt(buffer, matrix, from, to, cameraPos, arc.origin, arc.age);
            }
            if (arc.age > 10) {
                arc.segments.clear();
                for (int idx = 0; idx < ARC_SEGMENTS; idx++) {
                    arc.segments.add(arc.origin.add(
                            (Math.random() - 0.5) * ARC_RANGE,
                            (Math.random() - 0.5) * ARC_RANGE,
                            (Math.random() - 0.5) * ARC_RANGE
                    ));
                }
                arc.age = 0;
            }
        }

        matrices.pop();*/
    }

    private static void drawTriangularBolt(VertexConsumer buffer, Matrix4f matrix, Vec3d from, Vec3d to, Vec3d cameraPos, Vec3d origin, int age) {
        Vec3d dir = to.subtract(from).normalize();

        Vec3d up = new Vec3d(0, 1, 0);
        Vec3d side = dir.crossProduct(up).normalize().multiply(0.05); // Adjust thickness here (0.05)

        if (side.lengthSquared() < 1e-6) {
            side = dir.crossProduct(new Vec3d(1, 0, 0)).normalize().multiply(0.05);
        }

        double fromDist = from.distanceTo(origin);
        double toDist = to.distanceTo(origin);
        double maxDist = 2.0;
        float fromBlend = (float) Math.max(0, 1.0 - fromDist / maxDist);
        float toBlend = (float) Math.max(0, 1.0 - toDist / maxDist);

        int baseR = 170, baseG = 60, baseB = 255;
        int alpha = Math.min(255, 100 + age * 15);

        int fromR = (int)(baseR + (255 - baseR) * fromBlend);
        int fromG = (int)(baseG + (255 - baseG) * fromBlend);
        int fromB = (int)(baseB + (255 - baseB) * fromBlend);

        int toR = (int)(baseR + (255 - baseR) * toBlend);
        int toG = (int)(baseG + (255 - baseG) * toBlend);
        int toB = (int)(baseB + (255 - baseB) * toBlend);

        buffer.vertex(matrix, (float)(from.x - cameraPos.x - side.x), (float)(from.y - cameraPos.y - side.y), (float)(from.z - cameraPos.z - side.z))
                .color(fromR, fromG, fromB, alpha)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(0xF000F0)
                .normal(0f, 1f, 0f)
                .next();

        buffer.vertex(matrix, (float)(to.x - cameraPos.x - side.x), (float)(to.y - cameraPos.y - side.y), (float)(to.z - cameraPos.z - side.z))
                .color(toR, toG, toB, alpha)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(0xF000F0)
                .normal(0f, 1f, 0f)
                .next();

        buffer.vertex(matrix, (float)(from.x - cameraPos.x + side.x), (float)(from.y - cameraPos.y + side.y), (float)(from.z - cameraPos.z + side.z))
                .color(fromR, fromG, fromB, alpha)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(0xF000F0)
                .normal(0f, 1f, 0f)
                .next();

        buffer.vertex(matrix, (float)(to.x - cameraPos.x + side.x), (float)(to.y - cameraPos.y + side.y), (float)(to.z - cameraPos.z + side.z))
                .color(toR, toG, toB, alpha)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(0xF000F0)
                .normal(0f, 1f, 0f)
                .next();
    }

    private static void drawThickLineWithFading(VertexConsumer buffer, Matrix4f matrix, Vec3d from, Vec3d to, Vec3d cameraPos, Vec3d origin, int age) {
        Vec3d lineDir = to.subtract(from).normalize();
        Vec3d up = new Vec3d(0, 1, 0);
        Vec3d side = lineDir.crossProduct(up).normalize().multiply(0.03); // thickness ~ 0.03

        drawLineWithFading(buffer, matrix, from.add(side), to.add(side), cameraPos, origin, age);
        drawLineWithFading(buffer, matrix, from.subtract(side), to.subtract(side), cameraPos, origin, age);
        drawLineWithFading(buffer, matrix, from, to, cameraPos, origin, age);
    }

    private static void drawLineWithFading(VertexConsumer buffer, Matrix4f matrix, Vec3d from, Vec3d to, Vec3d cameraPos, Vec3d origin, int age) {
        double fromDist = from.distanceTo(origin);
        double toDist = to.distanceTo(origin);
        double maxDist = 2.0;
        float fromBlend = (float) Math.max(0, 1.0 - fromDist / maxDist);
        float toBlend = (float) Math.max(0, 1.0 - toDist / maxDist);

        int baseR = 170, baseG = 60, baseB = 255;
        int alpha = Math.min(255, 100 + age * 15);

        int fromR = (int)(baseR + (255 - baseR) * fromBlend);
        int fromG = (int)(baseG + (255 - baseG) * fromBlend);
        int fromB = (int)(baseB + (255 - baseB) * fromBlend);

        int toR = (int)(baseR + (255 - baseR) * toBlend);
        int toG = (int)(baseG + (255 - baseG) * toBlend);
        int toB = (int)(baseB + (255 - baseB) * toBlend);

        buffer.vertex(matrix, (float)(from.x - cameraPos.x), (float)(from.y - cameraPos.y), (float)(from.z - cameraPos.z))
                .color(fromR, fromG, fromB, alpha)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(0xF000F0)
                .normal(0f, 1f, 0f)
                .next();

        buffer.vertex(matrix, (float)(to.x - cameraPos.x), (float)(to.y - cameraPos.y), (float)(to.z - cameraPos.z))
                .color(toR, toG, toB, alpha)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(0xF000F0)
                .normal(0f, 1f, 0f)
                .next();
    }


    private static class LightningArc {
        public List<Vec3d> segments;
        public int age = 0;
        public Vec3d origin;

        public static LightningArc createRandomArc(Vec3d origin) {
            LightningArc arc = new LightningArc();
            arc.origin = origin;
            arc.segments = new ArrayList<>();
            arc.segments.add(origin);
            arc.segments.add(origin.add(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5));
            arc.segments.add(origin.add(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5));
            return arc;
        }
    }
}
