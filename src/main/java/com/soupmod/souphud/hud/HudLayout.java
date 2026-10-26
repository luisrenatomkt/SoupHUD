package com.soupmod.souphud.hud;

import com.soupmod.souphud.config.ElementSettings;

/**
 * Converte a posicao salva (fracao da tela) em pixels de GUI. O resultado e sempre limitado a area visivel
 * e arredondado para inteiro, o que evita tremores e elementos fora da tela em qualquer resolucao.
 */
public final class HudLayout {

    private HudLayout() {
    }

    public static int resolveX(ElementSettings s, int screenW, float widthPx) {
        return resolve(s.x, screenW, widthPx);
    }

    public static int resolveY(ElementSettings s, int screenH, float heightPx) {
        return resolve(s.y, screenH, heightPx);
    }

    private static int resolve(float fraction, int screen, float size) {
        float max = screen - size;
        if (max < 0.0F) {
            max = 0.0F;
        }
        float p = fraction * screen;
        if (p < 0.0F) {
            p = 0.0F;
        }
        if (p > max) {
            p = max;
        }
        return Math.round(p);
    }
}
