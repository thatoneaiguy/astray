package com.thatoneaiguy.archipelago.packet;

import com.thatoneaiguy.archipelago.util.HotbarRenderingUtil;
import me.pepperbell.simplenetworking.S2CPacket;
import me.pepperbell.simplenetworking.SimpleChannel;
import nakern.be_camera.camera.CameraData;
import nakern.be_camera.camera.CameraManager;
import nakern.be_camera.camera.EaseOptions;
import nakern.be_camera.easings.Easings;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class CameraModificationS2C implements S2CPacket {
    private final Vec3d target;
    private final Vec3d userPos;
    private final int duration;

    public CameraModificationS2C(Vec3d target, Vec3d userPos, int duration) {
        this.target = target;
        this.userPos = userPos;
        this.duration = duration;
    }

    public CameraModificationS2C(PacketByteBuf buf) {
        this.target = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
        this.userPos = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
        this.duration = buf.readVarInt();
    }

    @Override
    public void encode(PacketByteBuf buf) {
        buf.writeDouble(target.x);
        buf.writeDouble(target.y);
        buf.writeDouble(target.z);

        buf.writeDouble(userPos.x);
        buf.writeDouble(userPos.y);
        buf.writeDouble(userPos.z);

        buf.writeVarInt(duration);
    }

    @Override
    public void handle(MinecraftClient client, ClientPlayNetworkHandler handler, PacketSender sender, SimpleChannel channel) {
        client.execute(() -> {
            ClientPlayerEntity user = client.player;
            World world = client.world;
            if (world != null && user != null) {
                HotbarRenderingUtil.setHotbarRendered(false);

                Vec3d eyePos = user.getEyePos();
                Vec3d lookVec = user.getRotationVector();

                double maxDistance = 10.0;
                double minDistance = 3.0;
                double stepDistance = 2.0;
                double upOffset = 2.5;
                int maxAttempts = 5;
                boolean found = false;

                for (double distance = maxDistance; distance >= minDistance; distance -= stepDistance) {
                    for (int i = 0; i < maxAttempts; i++) {
                        double yawOffset = (i - maxAttempts / 2) * 5.0;
                        Vec3d rotatedVec = lookVec.rotateY((float) Math.toRadians(yawOffset));

                        Vec3d target = eyePos.add(rotatedVec.multiply(distance)).add(0, upOffset, 0);

                        BlockHitResult hit = world.raycast(new RaycastContext(
                                eyePos,
                                target,
                                RaycastContext.ShapeType.OUTLINE,
                                RaycastContext.FluidHandling.NONE,
                                user
                        ));

                        if (hit.getType() == HitResult.Type.MISS) {
                            if (world.isClient) {
                                CameraManager.INSTANCE.setCamera(
                                        new CameraData(
                                                target,
                                                user.getPos(),
                                                new EaseOptions(
                                                        Easings.INSTANCE::easeInQuint,
                                                        1500L
                                                )
                                        )
                                );
                            }
                            found = true;
                            break;
                        }
                    }
                    if (found) break;
                }
            }
        });
    }
}