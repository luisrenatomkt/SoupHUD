package com.soupmod.souphud.config;

/**
 * Configuracao de um elemento. x/y sao fracoes (0..1) da largura/altura da tela em coordenadas de GUI,
 * por isso a posicao se mantem proporcional em qualquer resolucao ou escala de GUI.
 */
public class ElementSettings {
    public boolean enabled = true;
    public float x = 0.01F;
    public float y = 0.30F;
    public float scale = 1.0F;
    /** Cor RGB (sem alpha). */
    public int color = 0xFFFFFF;
    /** 0.1 .. 1.0 */
    public float opacity = 1.0F;
    public DisplayMode displayMode = DisplayMode.TEXT;
    /** Usado por Sopas e Tigelas. */
    public CountMode countMode = CountMode.HOTBAR;
    /** Usados apenas pelo elemento Status. */
    public boolean showSoups = true;
    public boolean showBowls = true;

    public void sanitize(ElementType type) {
        x = clamp(x, 0.0F, 1.0F, type.getDefaultX());
        y = clamp(y, 0.0F, 1.0F, type.getDefaultY());
        scale = clamp(scale, 0.5F, 3.0F, 1.0F);
        opacity = clamp(opacity, 0.1F, 1.0F, 1.0F);
        color &= 0xFFFFFF;
        if (displayMode == null) {
            displayMode = type.getDefaultDisplayMode();
        }
        if (countMode == null) {
            countMode = type.getDefaultCountMode();
        }
    }

    private static float clamp(float v, float lo, float hi, float fallback) {
        if (Float.isNaN(v) || Float.isInfinite(v)) {
            return fallback;
        }
        return v < lo ? lo : (v > hi ? hi : v);
    }
}
