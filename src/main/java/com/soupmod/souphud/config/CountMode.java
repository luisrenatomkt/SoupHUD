package com.soupmod.souphud.config;

/** Define quais slots entram na contagem. */
public enum CountMode {
    HOTBAR("Hotbar"),
    INVENTORY("Invent\u00e1rio");

    private final String label;

    CountMode(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public CountMode next() {
        CountMode[] all = values();
        return all[(ordinal() + 1) % all.length];
    }
}
