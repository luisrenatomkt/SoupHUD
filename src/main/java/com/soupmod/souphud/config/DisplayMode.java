package com.soupmod.souphud.config;

/** Formato de exibicao de um elemento do HUD. */
public enum DisplayMode {
    TEXT("Texto"),
    ICON("\u00cdcone"),
    COMPACT("Compacto");

    private final String label;

    DisplayMode(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public DisplayMode next() {
        DisplayMode[] all = values();
        return all[(ordinal() + 1) % all.length];
    }
}
