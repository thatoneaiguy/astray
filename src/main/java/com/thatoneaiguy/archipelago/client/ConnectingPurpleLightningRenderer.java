package com.thatoneaiguy.archipelago.client;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ConnectingPurpleLightningRenderer {
    public static final List<LightningArc> activeArcs = new ArrayList<>();
    public static int ARC_LIFESPAN_TICKS = 15;

    public static void tick(Random random, Vec3d origin) {
        Iterator<LightningArc> iterator = activeArcs.iterator();
        while (iterator.hasNext()) {
            LightningArc arc = iterator.next();
            if (--arc.age <= 0) {
                iterator.remove();
            }
        }

        if (random.nextFloat() < 0.6f) {
            int boltCount = 2 + random.nextInt(2); // 2–3 arcs

            for (int i = 0; i < boltCount; i++) {
                Vec3d target = getRandomTarget(origin, 1.5f, random);
                List<Vec3d> segments = buildArcSegments(origin, target, random);
                int lifespan = ARC_LIFESPAN_TICKS + random.nextInt(3);
                activeArcs.add(new LightningArc(segments, lifespan));
            }
        }
    }

    public static void render(MatrixStack matrices, VertexConsumerProvider consumers, Vec3d cameraPos, Vec3d riftOrigin) {
        VertexConsumer buffer = consumers.getBuffer(RenderLayer.getLightning());
        Matrix4f matrix = matrices.peek().getPositionMatrix();

        for (LightningArc arc : activeArcs) {
            for (int i = 1; i < arc.segments.size(); i++) {
                Vec3d from = arc.segments.get(i - 1);
                Vec3d to = arc.segments.get(i);
                drawLineWithFading(buffer, matrix, from, to, cameraPos, riftOrigin, arc.age);
            }
        }
    }

    private static Vec3d getRandomTarget(Vec3d origin, float radius, Random random) {
        return origin.add(
                (random.nextFloat() - 0.5f) * 2 * radius,
                (random.nextFloat() - 0.2f) * 2 * radius,
                (random.nextFloat() - 0.5f) * 2 * radius
        );
    }

    private static List<Vec3d> buildArcSegments(Vec3d from, Vec3d to, Random random) {
        List<Vec3d> segments = new ArrayList<>();
        segments.add(from);
        int count = 5 + random.nextInt(3); // 5–7 segments
        for (int i = 1; i < count; i++) {
            double t = (double) i / count;
            segments.add(from.lerp(to, t).addRandom(random, 0.1f));
        }
        segments.add(to);
        return segments;
    }

    private static void drawLine(VertexConsumer buffer, Matrix4f matrix, Vec3d from, Vec3d to, Vec3d cameraPos, int age) {
        float sx = (float) (from.x - cameraPos.x);
        float sy = (float) (from.y - cameraPos.y);
        float sz = (float) (from.z - cameraPos.z);
        float ex = (float) (to.x - cameraPos.x);
        float ey = (float) (to.y - cameraPos.y);
        float ez = (float) (to.z - cameraPos.z);

        int alpha = Math.min(255, 100 + age * 15); // brighter when young
        buffer.vertex(matrix, sx, sy, sz).color(170, 60, 255, alpha).next();
        buffer.vertex(matrix, ex, ey, ez).color(170, 60, 255, alpha).next();
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
                .color(fromR, fromG, fromB, alpha).next();

        buffer.vertex(matrix, (float)(to.x - cameraPos.x), (float)(to.y - cameraPos.y), (float)(to.z - cameraPos.z))
                .color(toR, toG, toB, alpha).next();
    }

    public static class LightningArc {
        public final List<Vec3d> segments;
        public int age;

        public LightningArc(List<Vec3d> segments, int age) {
            this.segments = segments;
            this.age = age;
        }
    }
}
