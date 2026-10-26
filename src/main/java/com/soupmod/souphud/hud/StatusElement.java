package com.soupmod.souphud.hud;

import com.soupmod.souphud.config.ElementSettings;
import com.soupmod.souphud.config.ElementType;
import com.soupmod.souphud.config.HudConfig;
import com.soupmod.souphud.counter.InventoryScanner;

/**
 * Sopas e tigelas em um unico indicador. Cada parte pode ser ocultada (showSoups/showBowls).
 * Usa os modos de contagem configurados nos elementos Sopas e Tigelas.
 */
public final class StatusElement extends HudElement {

    public StatusElement() {
        super(ElementType.STATUS);
    }

    @Override
    protected void build(HudConfig cfg, ElementSettings s, InventoryScanner scanner) {
        int soups = scanner.getSoups(cfg.soup.countMode);
        int bowls = scanner.getBowls(cfg.bowl.countMode);
        boolean low = cfg.lowSoupAlert && soups <= cfg.lowSoupThreshold;
        int soupColor = low ? cfg.alertColor : s.color;
        String soupText = Integer.toString(soups);
        String bowlText = Integer.toString(bowls);

        if (s.showSoups && low) {
            markAlert();
        }

        switch (s.displayMode) {
            case ICON:
                if (s.showSoups) {
                    add(HudIcons.soup(), soupText, soupColor);
                }
                if (s.showBowls) {
                    add(HudIcons.bowl(), bowlText, s.color);
                }
                break;
            case TEXT:
                if (s.showSoups) {
                    add(null, "Sopas:", LABEL_COLOR);
                    add(null, soupText, soupColor);
                }
                if (s.showSoups && s.showBowls) {
                    add(null, "|", SEPARATOR_COLOR);
                }
                if (s.showBowls) {
                    add(null, "Tigelas:", LABEL_COLOR);
                    add(null, bowlText, s.color);
                }
                break;
            default:
                if (s.showSoups) {
                    add(null, soupText, soupColor);
                }
                if (s.showSoups && s.showBowls) {
                    add(null, "/", SEPARATOR_COLOR);
                }
                if (s.showBowls) {
                    add(null, bowlText, s.color);
                }
                break;
        }
    }
}
