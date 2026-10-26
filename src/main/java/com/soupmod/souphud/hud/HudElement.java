package com.soupmod.souphud.hud;

import com.soupmod.souphud.config.DisplayMode;
import com.soupmod.souphud.config.ElementSettings;
import com.soupmod.souphud.config.ElementType;
import com.soupmod.souphud.config.HudConfig;
import com.soupmod.souphud.counter.InventoryScanner;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;

/**
 * Base de um elemento do HUD. O conteudo (textos e largura) e montado em {@link #rebuild} somente quando
 * a configuracao ou as contagens mudam; o desenho por frame so le valores prontos.
 *
 * Para criar um elemento novo, estenda esta classe e implemente {@link #build}.
 */
public abstract class HudElement {

    public static final int LABEL_COLOR = 0xAAAAAA;
    public static final int SEPARATOR_COLOR = 0x777777;

    private static final int MAX_PARTS = 8;
    private static final int MIN_SIZE = 16;

    private final ElementType type;
    private final HudPart[] parts = new HudPart[MAX_PARTS];
    private int partCount;
    private int width = MIN_SIZE;
    private int height = 16;
    private int padding = 4;
    private int gap = 3;
    private boolean alert;
    private FontRenderer building;

    protected HudElement(ElementType type) {
        this.type = type;
        for (int i = 0; i < MAX_PARTS; i++) {
            parts[i] = new HudPart();
        }
    }

    public final void rebuild(FontRenderer font, HudConfig cfg, InventoryScanner scanner) {
        ElementSettings s = cfg.get(type);
        this.building = font;
        this.partCount = 0;
        this.alert = false;

        boolean compact = s.displayMode == DisplayMode.COMPACT;
        this.padding = compact ? 3 : 4;
        this.gap = compact ? 2 : 3;
        this.height = compact ? 14 : 16;

        build(cfg, s, scanner);

        if (partCount == 0) {
            this.width = MIN_SIZE;
        } else {
            int w = 0;
            for (int i = 0; i < partCount; i++) {
                w += parts[i].getWidth();
            }
            w += gap * (partCount - 1);
            this.width = w + padding * 2;
        }
        this.building = null;
    }

    /** Monte o elemento chamando {@link #add} e, se necessario, {@link #markAlert}. */
    protected abstract void build(HudConfig cfg, ElementSettings settings, InventoryScanner scanner);

    protected final void add(ItemStack icon, String text, int rgbColor) {
        if (partCount < MAX_PARTS) {
            parts[partCount++].set(icon, text, rgbColor, building);
        }
    }

    protected final void markAlert() {
        this.alert = true;
    }

    public final ElementType getType() {
        return type;
    }

    public final int getWidth() {
        return width;
    }

    public final int getHeight() {
        return height;
    }

    public final int getPadding() {
        return padding;
    }

    public final int getGap() {
        return gap;
    }

    public final int getPartCount() {
        return partCount;
    }

    public final HudPart getPart(int index) {
        return parts[index];
    }

    public final boolean isAlert() {
        return alert;
    }

    public final boolean hasContent() {
        return partCount > 0;
    }
}
