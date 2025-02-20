package com.thatoneaiguy.archipelago.mixin.vfx;

import com.thatoneaiguy.archipelago.Archipelago;
import com.thatoneaiguy.archipelago.util.LocationHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.client.world.ClientWorld.Properties;
import net.minecraft.world.HeightLimitView;

@Mixin(value = WorldRenderer.class, priority = 1002)
public class WorldRendererMixin {

    @Shadow private ClientWorld world;
    @Shadow private MinecraftClient client;

    /*
    @Unique private CustomVoidConfig VOID_CONFIG = CustomVoid.VOID_CONFIG;

    @Redirect(
            method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/math/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V",
                    ordinal = 5
            )
    )
    private void modifyVoidColor(float r, float g, float b, float a) {
        Vec3d skyColor = this.world.getSkyColor(this.client.gameRenderer.getCamera().getPos(), 0.1f);
        float x = (float) skyColor.x;
        float y = (float) skyColor.y;
        float z = (float) skyColor.z;

        float cR = VOID_CONFIG.voidCustomRed;
        float cG = VOID_CONFIG.voidCustomGreen;
        float cB = VOID_CONFIG.voidCustomBlue;

        RenderSystem.setShaderColor(cR / 255F, cG / 255F, cB / 255F, a);
    }

    @Redirect(
            method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/math/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V",
            at = @At(
                    value = "INVOKE",
                    target = "net/minecraft/client/util/math/MatrixStack.translate (DDD)V"
            )
    )
    private void modifyVoidTranslation(MatrixStack instance, double x, double y, double z) {
        // y = 12 by default
        y += 0;

        if (VOID_CONFIG.voidDynamicHeight) {
            double voidOffset = Math.max(this.client.gameRenderer.getCamera().getPos().y - 65f, 0f);

            y += -voidOffset;
        }

        instance.translate(x, y, z);
    }*/

    @Redirect(
            method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lorg/joml/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/world/ClientWorld$Properties;getSkyDarknessHeight(Lnet/minecraft/world/HeightLimitView;)D"
            )
    )
    private double modifyVoidThreshold(Properties self, HeightLimitView world) {
        if ( LocationHelper.isPlayerInArchipelago(client.player) ) {
            return -100;
        }
        return -64;
    }
}
