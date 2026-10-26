package com.soupmod.souphud.hud;

import com.soupmod.souphud.config.ElementSettings;
import com.soupmod.souphud.config.ElementType;
import com.soupmod.souphud.config.HudConfig;
import com.soupmod.souphud.counter.InventoryScanner;

/** Contador de sopas com alerta discreto (cor + "!") quando a quantidade esta baixa. */
public final class SoupElement extends HudElement {

    public SoupElement() {
        super(ElementType.SOUP);
    }

    @Override
    protected void build(HudConfig cfg, ElementSettings s, InventoryScanner scanner) {
        int count = scanner.getSoups(s.countMode);
        boolean low = cfg.lowSoupAlert && count <= cfg.lowSoupThreshold;
        int color = low ? cfg.alertColor : s.color;
        String value = low ? count + " !" : Integer.toString(count);
        if (low) {
            markAlert();
        }
        switch (s.displayMode) {
            case ICON:
                add(HudIcons.soup(), value, color);
                break;
            case COMPACT:
                add(null, "S", LABEL_COLOR);
                add(null, value, color);
                break;
            default:
                add(null, "Sopas:", LABEL_COLOR);
                add(null, value, color);
                break;
        }
    }
}
