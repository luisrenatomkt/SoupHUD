package com.soupmod.souphud.hud;

import com.soupmod.souphud.config.ElementSettings;
import com.soupmod.souphud.config.ElementType;
import com.soupmod.souphud.config.HudConfig;
import com.soupmod.souphud.counter.InventoryScanner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

/**
 * Coordena os elementos do HUD. Os textos so sao reconstruidos quando a configuracao, as contagens
 * ou a fonte mudam; nos demais frames apenas desenha.
 *
 * Para adicionar um elemento: veja a secao "Como adicionar novos elementos" do README.
 */
public final class HudManager {

    private final Minecraft mc = Minecraft.getMinecraft();
    private final HudConfig config;
    private final InventoryScanner scanner;
    private final HudRenderer renderer = new HudRenderer();
    private final HudElement[] elements;

    private int lastConfigRevision = -1;
    private int lastScanRevision = -1;
    private boolean lastUnicode;

    public HudManager(HudConfig config, InventoryScanner scanner) {
        this.config = config;
        this.scanner = scanner;
        // Ordem = ordem de desenho. Registre novos elementos aqui.
        this.elements = new HudElement[]{
                new SoupElement(),
                new BowlElement(),
                new FreeSlotsElement(),
                new StatusElement()
        };
    }

    public HudElement[] getElements() {
        return elements;
    }

    public HudElement getElement(ElementType type) {
        for (int i = 0; i < elements.length; i++) {
            if (elements[i].getType() == type) {
                return elements[i];
            }
        }
        return null;
    }

    public HudRenderer getRenderer() {
        return renderer;
    }

    public HudConfig getConfig() {
        return config;
    }

    /** Reconstroi os elementos somente se algo mudou. */
    public void ensureFresh() {
        FontRenderer font = mc.fontRendererObj;
        if (font == null) {
            return;
        }
        boolean unicode = font.getUnicodeFlag();
        if (config.getRevision() == lastConfigRevision
                && scanner.getRevision() == lastScanRevision
                && unicode == lastUnicode) {
            return;
        }
        for (int i = 0; i < elements.length; i++) {
            elements[i].rebuild(font, config, scanner);
        }
        lastConfigRevision = config.getRevision();
        lastScanRevision = scanner.getRevision();
        lastUnicode = unicode;
    }

    /** Desenho em jogo (overlay). */
    public void renderOverlay(int screenW, int screenH) {
        if (!config.enabled) {
            return;
        }
        ensureFresh();
        for (int i = 0; i < elements.length; i++) {
            HudElement element = elements[i];
            ElementSettings s = config.get(element.getType());
            if (!s.enabled || !element.hasContent()) {
                continue;
            }
            renderer.draw(element, s, config, screenW, screenH, 1.0F);
        }
    }
}
