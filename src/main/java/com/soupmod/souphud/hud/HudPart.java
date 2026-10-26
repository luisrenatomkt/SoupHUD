package com.soupmod.souphud.hud;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;

/** Um pedaco desenhavel de um elemento: icone opcional + texto opcional. Reutilizado entre reconstrucoes. */
public final class HudPart {

    public static final int ICON_SIZE = 12;
    public static final int ICON_GAP = 2;

    private ItemStack icon;
    private String text;
    private int color;
    private int textWidth;
    private int width;

    void set(ItemStack icon, String text, int color, FontRenderer font) {
        this.icon = icon;
        this.text = text;
        this.color = color & 0xFFFFFF;
        this.textWidth = text != null ? font.getStringWidth(text) : 0;
        int w = textWidth;
        if (icon != null) {
            w += ICON_SIZE + (text != null ? ICON_GAP : 0);
        }
        this.width = w;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public String getText() {
        return text;
    }

    public int getColor() {
        return color;
    }

    public int getTextWidth() {
        return textWidth;
    }

    public int getWidth() {
        return width;
    }
}
