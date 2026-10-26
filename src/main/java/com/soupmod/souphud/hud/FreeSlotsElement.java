package com.soupmod.souphud.hud;

import com.soupmod.souphud.config.ElementSettings;
import com.soupmod.souphud.config.ElementType;
import com.soupmod.souphud.config.HudConfig;
import com.soupmod.souphud.counter.InventoryScanner;

/** Espacos livres do inventario; mostra "CHEIO" em cor de alerta quando nao ha mais espaco. */
public final class FreeSlotsElement extends HudElement {

    public FreeSlotsElement() {
        super(ElementType.FREE_SLOTS);
    }

    @Override
    protected void build(HudConfig cfg, ElementSettings s, InventoryScanner scanner) {
        int free = scanner.getFreeSlots();
        boolean full = free == 0;
        String value = full ? "CHEIO" : Integer.toString(free);
        int color = full ? cfg.alertColor : s.color;
        if (full) {
            markAlert();
        }
        switch (s.displayMode) {
            case ICON:
                add(HudIcons.chest(), value, color);
                break;
            case COMPACT:
                add(null, "L", LABEL_COLOR);
                add(null, value, color);
                break;
            default:
                add(null, "Livres:", LABEL_COLOR);
                add(null, value, color);
                break;
        }
    }
}
