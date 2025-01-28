package com.thatoneaiguy.archipelago.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.thatoneaiguy.archipelago.init.ArchipelagoItems;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class GlintItemRenderer extends ItemRenderer {
    private static final Identifier ENCHANT_GLOW = new Identifier("archipelago", "textures/misc/enchant_overlay.png");

    public GlintItemRenderer(TextureManager manager, BakedModelManager bakery, ItemColors colors, BuiltinModelItemRenderer builtinModelItemRenderer) {
        super(manager, bakery, colors, builtinModelItemRenderer);
    }

    @Override
    public void renderGuiItemOverlay(TextRenderer renderer, ItemStack stack, int x, int y) {
        MatrixStack matrixStack = new MatrixStack();

        if (shouldRenderEnchantOverlay(stack)) {
            renderOverlay(matrixStack, x, y);
        }

        super.renderGuiItemOverlay(renderer, stack, x, y);
    }

    private boolean shouldRenderEnchantOverlay(ItemStack stack) {
        return stack.isOf(ArchipelagoItems.STARWEAVER);
    }

    private void renderOverlay(DrawContext context, int x, int y) {
        RenderSystem.setShaderTexture(0, ENCHANT_GLOW);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();

        context.drawTexture(ENCHANT_GLOW, x, y, 0, 0, 0, 0, 16, 16);

        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }
}
