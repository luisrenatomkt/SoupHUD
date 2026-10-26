package com.soupmod.souphud.config;

/** Elementos do HUD e seus valores padrao (posicao em fracao da tela, cor, modos). */
public enum ElementType {
    SOUP("Sopas", 0.010F, 0.30F, 0x55FF55, DisplayMode.TEXT, CountMode.HOTBAR),
    BOWL("Tigelas", 0.010F, 0.36F, 0xFFAA00, DisplayMode.TEXT, CountMode.INVENTORY),
    FREE_SLOTS("Espa\u00e7os livres", 0.010F, 0.42F, 0x55FFFF, DisplayMode.TEXT, CountMode.INVENTORY),
    STATUS("Status", 0.010F, 0.24F, 0xFFFFFF, DisplayMode.COMPACT, CountMode.HOTBAR);

    private final String displayName;
    private final float defaultX;
    private final float defaultY;
    private final int defaultColor;
    private final DisplayMode defaultDisplayMode;
    private final CountMode defaultCountMode;

    ElementType(String displayName, float defaultX, float defaultY, int defaultColor,
                DisplayMode defaultDisplayMode, CountMode defaultCountMode) {
        this.displayName = displayName;
        this.defaultX = defaultX;
        this.defaultY = defaultY;
        this.defaultColor = defaultColor;
        this.defaultDisplayMode = defaultDisplayMode;
        this.defaultCountMode = defaultCountMode;
    }

    public String getDisplayName() {
        return displayName;
    }

    public float getDefaultX() {
        return defaultX;
    }

    public float getDefaultY() {
        return defaultY;
    }

    public int getDefaultColor() {
        return defaultColor;
    }

    public DisplayMode getDefaultDisplayMode() {
        return defaultDisplayMode;
    }

    public CountMode getDefaultCountMode() {
        return defaultCountMode;
    }

    public ElementSettings createDefaults() {
        ElementSettings s = new ElementSettings();
        s.x = defaultX;
        s.y = defaultY;
        s.color = defaultColor;
        s.displayMode = defaultDisplayMode;
        s.countMode = defaultCountMode;
        return s;
    }

    public ElementType next() {
        ElementType[] all = values();
        return all[(ordinal() + 1) % all.length];
    }
}
