package com.thatoneaiguy.archipelago.mixin.hud;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.systems.RenderSystem;
import com.thatoneaiguy.archipelago.Archipelago;
import com.thatoneaiguy.archipelago.util.LocationHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.client.gui.DrawableHelper.drawTexture;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Shadow
    @Final
    MinecraftClient client;
    @Unique private float lastHealth = -1.0F;
    @Unique private int healTicksRemaining = 0;

    /**
     * i don't like how the useless bars looks, so this hides them if you
     * are in the archipelago.
     */
    @WrapMethod(
            method = "renderStatusBars"
    )
    private void archipelago$renderStatusBars(MatrixStack matrices, Operation<Void> original) {
        if (!LocationHelper.isPlayerInArchipelago(client.player)) {
            original.call(matrices);
        }
    }

    /**
     * so it felt weird only having the effect ui elements on screen
     * and im planning on using effects to manage some things
     * so... bye bye!!!
     */
    @WrapMethod(
            method = "renderStatusEffectOverlay"
    )
    private void archipelago$renderStatusEffectOverlay(MatrixStack matrices, Operation<Void> original) {
        if (!LocationHelper.isPlayerInArchipelago(client.player)) {
            original.call(matrices);
        }
    }

    /**
     * people would probably get angry if they had no way to see their health.
     * all methods till the next method description are helping the XP bar become your
     * health bar
     */
    @Inject(method = "renderExperienceBar", at = @At("HEAD"), cancellable = true)
    private void archipelago$modifyExperienceBar(MatrixStack matrices, int x, CallbackInfo ci) {
        if (LocationHelper.isPlayerInArchipelago(client.player)) {
            MinecraftClient client = MinecraftClient.getInstance();
            PlayerEntity player = client.player;

            if (player == null) return;

            ci.cancel();

            float currentHealth = player.getHealth();
            float absorptionHealth = player.getAbsorptionAmount();
            float totalHealth = currentHealth + absorptionHealth;

            float mainHealth = Math.min(totalHealth, 20.0F);
            float extraHealth = Math.max(totalHealth - 20.0F, 0.0F);

            float mainHealthPercentage = mainHealth / 20.0F;

            if (lastHealth >= 0 && totalHealth > lastHealth) healTicksRemaining = 65;
            lastHealth = totalHealth;
            if (healTicksRemaining > 0) healTicksRemaining--;

            boolean stayGreen = healTicksRemaining > 0;
            renderModifiedXpBar(matrices, x, mainHealthPercentage, stayGreen);

            if (extraHealth > 0) {
                renderExtraHealthBar(matrices, x, extraHealth);
            }
        }
    }

    @Unique
    private void renderModifiedXpBar(MatrixStack matrices, int x, float healthPercentage, boolean stayGreen) {
        MinecraftClient client = MinecraftClient.getInstance();
        int yPos = client.getWindow().getScaledHeight() - 32;
        int barWidth = 182;
        int filledWidth = (int) (barWidth * healthPercentage);

        int lowHealthColor = 0xFFFF1947;
        int highHealthColor = 0xFFC33B20;
        int healingColor = 0xFF91C848;

        int barColor = stayGreen ? healingColor : interpolateColor(lowHealthColor, highHealthColor, healthPercentage);

        client.getTextureManager().bindTexture(new Identifier("textures/gui/icons.png"));

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        drawTexture(matrices, x, yPos, 0, 64, barWidth, 5, 256, 256);

        float r = ((barColor >> 16) & 0xFF) / 255.0F;
        float g = ((barColor >> 8) & 0xFF) / 255.0F;
        float b = (barColor & 0xFF) / 255.0F;
        RenderSystem.setShaderColor(r, g, b, 1.0F);
        drawTexture(matrices, x, yPos, 0, 69, filledWidth, 5, 256, 256);

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    /**
     * manages the blue extra health bar
     * yes this is a fortnite shield reference
     */
    @Unique
    private void renderExtraHealthBar(MatrixStack matrices, int x, float extraHealth) {
        MinecraftClient client = MinecraftClient.getInstance();
        int yPos = client.getWindow().getScaledHeight() - 32;
        int barWidth = 182;

        float extraHealthPercentage = Math.min(extraHealth / 20.0F, 1.0F);
        int filledWidth = (int) (barWidth * extraHealthPercentage);

        int blueColor = 0xFF5865F2;

        client.getTextureManager().bindTexture(new Identifier("textures/gui/icons.png"));
        RenderSystem.setShader(GameRenderer::getPositionTexShader);

        float r = ((blueColor >> 16) & 0xFF) / 255.0F;
        float g = ((blueColor >> 8) & 0xFF) / 255.0F;
        float b = (blueColor & 0xFF) / 255.0F;
        RenderSystem.setShaderColor(r, g, b, 1.0F);
        drawTexture(matrices, x, yPos, 0, 69, filledWidth, 5, 256, 256);

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    /**
     * still for the health bar, but should probably specify that this manages the
     * colour interpolation
     */
    @Unique
    private int interpolateColor(int startColor, int endColor, float percentage) {
        int startRed = (startColor >> 16) & 0xFF;
        int startGreen = (startColor >> 8) & 0xFF;
        int startBlue = startColor & 0xFF;

        int endRed = (endColor >> 16) & 0xFF;
        int endGreen = (endColor >> 8) & 0xFF;
        int endBlue = endColor & 0xFF;

        int newRed = (int) (startRed + (endRed - startRed) * percentage);
        int newGreen = (int) (startGreen + (endGreen - startGreen) * percentage);
        int newBlue = (int) (startBlue + (endBlue - startBlue) * percentage);

        return (0xFF << 24) | (newRed << 16) | (newGreen << 8) | newBlue;
    }
}
