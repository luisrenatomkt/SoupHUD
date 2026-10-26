package com.soupmod.souphud.gui;

/** Paleta de cores do menu. Cores personalizadas podem ser definidas no souphud.json (inteiro RGB). */
public final class ColorPresets {

    private static final String[] NAMES = {
            "Branco", "Verde", "Vermelho", "Amarelo", "Ciano", "Laranja", "Rosa", "Azul", "Roxo", "Cinza"
    };
    private static final int[] COLORS = {
            0xFFFFFF, 0x55FF55, 0xFF5555, 0xFFFF55, 0x55FFFF, 0xFFAA00, 0xFF55FF, 0x5555FF, 0xAA00AA, 0xAAAAAA
    };

    private ColorPresets() {
    }

    private static int indexOf(int rgb) {
        int c = rgb & 0xFFFFFF;
        for (int i = 0; i < COLORS.length; i++) {
            if (COLORS[i] == c) {
                return i;
            }
        }
        return -1;
    }

    public static int next(int rgb) {
        return COLORS[(indexOf(rgb) + 1) % COLORS.length];
    }

    public static String nameOf(int rgb) {
        int i = indexOf(rgb);
        return i < 0 ? "Personalizada" : NAMES[i];
    }
}
