package com.thatoneaiguy.archipelago.util;


import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.ShapeType;
import net.minecraft.util.hit.BlockHitResult;

public class RaycastHelper {

    /**
     * Performs a raycast from the player's camera view.
     *
     * @return the BlockPos and distance to the hit block, or null if no block is hit.
     */
    public static RaycastResult raycast(double range) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;

        if (player == null) {
            return null;
        }

        // Get the player's camera position and view direction
        Vec3d start = player.getCameraPosVec(1.0f);  // 1.0f is the partial ticks parameter
        Vec3d direction = player.getRotationVector();

        // Perform the raycast with a max range of 25 blocks
        RaycastContext context = new RaycastContext(start, start.add(direction.multiply(range, range, range)),
                ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, player);
        BlockHitResult hitResult = player.getWorld().raycast(context);

        // If a block is hit, return the block position and the distance
        if (hitResult != null && hitResult.getType() == BlockHitResult.Type.BLOCK) {
            BlockPos blockPos = hitResult.getBlockPos();
            double distance = start.distanceTo(new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ()));
            return new RaycastResult(blockPos, distance);
        }

        // No block was hit
        return null;
    }

    // A simple result class to hold the position and distance of the hit block
    public static class RaycastResult {
        private final BlockPos blockPos;
        private final double distance;

        public RaycastResult(BlockPos blockPos, double distance) {
            this.blockPos = blockPos;
            this.distance = distance;
        }

        public BlockPos getBlockPos() {
            return blockPos;
        }

        public double getDistance() {
            return distance;
        }
    }
}
