package com.soupmod.souphud.hud;

import com.soupmod.souphud.config.ElementSettings;
import com.soupmod.souphud.config.ElementType;
import com.soupmod.souphud.config.HudConfig;
import com.soupmod.souphud.counter.InventoryScanner;

/** Contador de tigelas vazias. */
public final class BowlElement extends HudElement {

    public BowlElement() {
        super(ElementType.BOWL);
    }

    @Override
    protected void build(HudConfig cfg, ElementSettings s, InventoryScanner scanner) {
        String value = Integer.toString(scanner.getBowls(s.countMode));
        switch (s.displayMode) {
            case ICON:
                add(HudIcons.bowl(), value, s.color);
                break;
            case COMPACT:
                add(null, "T", LABEL_COLOR);
                add(null, value, s.color);
                break;
            default:
                add(null, "Tigelas:", LABEL_COLOR);
                add(null, value, s.color);
                break;
        }
    }
}
