package com.thatoneaiguy.archipelago.item.debug;

import com.thatoneaiguy.archipelago.util.HotbarRenderingUtil;
import nakern.be_camera.camera.CameraData;
import nakern.be_camera.camera.CameraManager;
import nakern.be_camera.camera.EaseOptions;
import nakern.be_camera.easings.Easings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class CameraModificationDebugItem extends Item {
    public CameraModificationDebugItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (user.isSneaking()) {
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
                                                    Easings.INSTANCE::easeInOutSine,
                                                    1500
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
        else {
            HotbarRenderingUtil.setHotbarRendered(true);
            CameraManager.INSTANCE.clear();
        }

        return super.use(world, user, hand);
    }
}
