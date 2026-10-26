package com.soupmod.souphud.hud;

import com.soupmod.souphud.config.ElementSettings;
import com.soupmod.souphud.config.HudConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

/** Sistema de renderizacao: desenha um elemento ja montado, sem alocar objetos. */
public final class HudRenderer {

    private static final float ICON_SCALE = 0.75F;
    private static final float BACKGROUND_FACTOR = 0.55F;

    private final Minecraft mc = Minecraft.getMinecraft();

    /**
     * @param alphaMultiplier 1.0 normal; menor que 1.0 para pre-visualizar elementos desativados.
     */
    public void draw(HudElement element, ElementSettings s, HudConfig cfg, int screenW, int screenH,
                     float alphaMultiplier) {
        FontRenderer font = mc.fontRendererObj;
        if (font == null) {
            return;
        }
        float scale = s.scale;
        int w = element.getWidth();
        int h = element.getHeight();
        int px = HudLayout.resolveX(s, screenW, w * scale);
        int py = HudLayout.resolveY(s, screenH, h * scale);
        int alpha = toAlpha(s.opacity * alphaMultiplier);

        GlStateManager.pushMatrix();
        GlStateManager.translate((float) px, (float) py, 0.0F);
        GlStateManager.scale(scale, scale, 1.0F);

        if (cfg.showBackground) {
            fillRounded(0, 0, w, h, Math.round(alpha * BACKGROUND_FACTOR) << 24);
        }
        if (element.isAlert()) {
            Gui.drawRect(2, h - 1, w - 2, h, (alpha << 24) | cfg.alertColor);
        }

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

        int x = element.getPadding();
        int textY = (h - 8) / 2;
        int iconY = (h - HudPart.ICON_SIZE) / 2;
        int textAlpha = alpha << 24;
        int gap = element.getGap();
        int count = element.getPartCount();

        for (int i = 0; i < count; i++) {
            HudPart part = element.getPart(i);
            if (part.getIcon() != null) {
                drawIcon(part.getIcon(), x, iconY);
                x += HudPart.ICON_SIZE;
                if (part.getText() != null) {
                    x += HudPart.ICON_GAP;
                }
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            }
            if (part.getText() != null) {
                font.drawStringWithShadow(part.getText(), (float) x, (float) textY, textAlpha | part.getColor());
                x += part.getTextWidth();
            }
            x += gap;
        }

        GlStateManager.popMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableAlpha();
    }

    /** Contorno de 1 px, em coordenadas de tela. */
    public void drawOutline(int x, int y, int w, int h, int argb) {
        Gui.drawRect(x - 1, y - 1, x + w + 1, y, argb);
        Gui.drawRect(x - 1, y + h, x + w + 1, y + h + 1, argb);
        Gui.drawRect(x - 1, y, x, y + h, argb);
        Gui.drawRect(x + w, y, x + w + 1, y + h, argb);
    }

    private void drawIcon(ItemStack stack, int x, int y) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y, 0.0F);
        GlStateManager.scale(ICON_SCALE, ICON_SCALE, 1.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        RenderHelper.enableGUIStandardItemLighting();
        mc.getRenderItem().renderItemAndEffectIntoGUI(stack, 0, 0);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    private static void fillRounded(int left, int top, int right, int bottom, int argb) {
        if ((argb >>> 24) == 0) {
            return;
        }
        Gui.drawRect(left + 1, top, right - 1, bottom, argb);
        Gui.drawRect(left, top + 1, left + 1, bottom - 1, argb);
        Gui.drawRect(right - 1, top + 1, right, bottom - 1, argb);
    }

    private static int toAlpha(float opacity) {
        int a = Math.round(opacity * 255.0F);
        if (a < 20) {
            return 20;
        }
        return a > 255 ? 255 : a;
    }
}
