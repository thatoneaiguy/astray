package com.thatoneaiguy.archipelago.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class CameraUtils {
    private static boolean active = false;
    private static Vec3d targetPosition = null;
    private static Entity targetEntity = null;
    private static double lerpSpeed = 0.1; // Speed of interpolation

    public static void moveTo(Vec3d position, double speed) {
        active = true;
        targetEntity = null;
        targetPosition = position;
        lerpSpeed = speed;
    }

    public static void lockOnto(Entity entity, double speed) {
        active = true;
        targetEntity = entity;
        lerpSpeed = speed;
    }

    public static void reset() {
        active = false;
        targetPosition = null;
        targetEntity = null;
    }

    public static void updateCamera(Camera camera) {
        if (!active) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        Vec3d currentPos = new Vec3d(camera.getPos().x, camera.getPos().y, camera.getPos().z);
        Vec3d targetPos = targetPosition;

        if (targetEntity != null) {
            targetPos = targetEntity.getPos().add(0, targetEntity.getHeight() + 1, 0);
        }

        if (targetPos != null) {
            Vec3d newPos = currentPos.lerp(targetPos, lerpSpeed);
            camera.setPos(newPos.x, newPos.y, newPos.z);
        }
    }
}
