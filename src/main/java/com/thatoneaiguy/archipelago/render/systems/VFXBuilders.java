package com.thatoneaiguy.archipelago.render.systems;

import com.thatoneaiguy.archipelago.util.RenderHelper;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class VFXBuilders {

    public static WorldVFXBuilder createWorld() {
        return new WorldVFXBuilder();
    }

    public static class WorldVFXBuilder {
        protected float r = 1, g = 1, b = 1, a = 1;
        protected float xOffset = 0, yOffset = 0, zOffset = 0;
        protected float u0 = 0, v0 = 0, u1 = 1, v1 = 1;
        protected WorldVertexPlacementSupplier supplier;

        public WorldVFXBuilder setColorWithAlpha(Color color) {
            return setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()/255f);
        }

        public WorldVFXBuilder setColor(Color color) {
            return setColor(color.getRed(), color.getGreen(), color.getBlue());
        }

        public WorldVFXBuilder setColor(Color color, float a) {
            return setColor(color).setAlpha(a);
        }

        public WorldVFXBuilder setColor(float r, float g, float b, float a) {
            return setColor(r, g, b).setAlpha(a);
        }

        public WorldVFXBuilder setColor(float r, float g, float b) {
            this.r = r / 255f;
            this.g = g / 255f;
            this.b = b / 255f;
            return this;
        }

        public WorldVFXBuilder setAlpha(float a) {
            this.a = a;
            return this;
        }

        public WorldVFXBuilder renderTrail(VertexConsumer vertexConsumer, MatrixStack stack, List<Vector4f> trailSegments, Function<Float, Float> widthFunc, Consumer<Float> vfxOperator) {
            return renderTrail(vertexConsumer, stack.peek().getPositionMatrix(), trailSegments, widthFunc, vfxOperator);
        }

        public WorldVFXBuilder renderTrail(VertexConsumer vertexConsumer, Matrix4f pose, List<Vector4f> trailSegments, Function<Float, Float> widthFunc, Consumer<Float> vfxOperator) {
            if (trailSegments.size() < 3) {
                return this;
            }
            trailSegments = trailSegments.stream().map(v -> new Vector4f(v.getX(), v.getY(), v.getZ(), v.getW())).collect(Collectors.toList());
            for (Vector4f pos : trailSegments) {
                pos.add(xOffset, yOffset, zOffset, 0);
                pos.transform(pose);
            }

            int count = trailSegments.size() - 1;
            float increment = 1.0F / (count - 1);
            ArrayList<TrailPoint> points = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                float width = widthFunc.apply(increment * i);
                Vector4f start = trailSegments.get(i);
                Vector4f end = trailSegments.get(i + 1);
                points.add(new TrailPoint(RenderHelper.midpoint(start, end), RenderHelper.screenSpaceQuadOffsets(start, end, width)));
            }
            return renderPoints(vertexConsumer, points, u0, v0, u1, v1, vfxOperator);
        }

        public WorldVFXBuilder renderPoints(VertexConsumer vertexConsumer, List<TrailPoint> trailPoints, float u0, float v0, float u1, float v1, Consumer<Float> vfxOperator) {
            int count = trailPoints.size() - 1;
            float increment = 1.0F / count;
            vfxOperator.accept(0f);
            trailPoints.get(0).renderStart(vertexConsumer, supplier, u0, v0, u1, MathHelper.lerp(increment, v0, v1));
            for (int i = 1; i < count; i++) {
                float current = MathHelper.lerp(i * increment, v0, v1);
                vfxOperator.accept(current);
                trailPoints.get(i).renderMid(vertexConsumer, supplier, u0, current, u1, current);
            }
            vfxOperator.accept(1f);
            trailPoints.get(count).renderEnd(vertexConsumer, supplier, u0, MathHelper.lerp((count) * increment, v0, v1), u1, v1);
            return this;
        }

        public interface WorldVertexPlacementSupplier {
            void placeVertex(VertexConsumer consumer, Matrix4f last, float x, float y, float z, float u, float v);
        }
    }
}
