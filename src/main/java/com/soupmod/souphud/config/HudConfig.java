package com.soupmod.souphud.config;

/** Modelo de configuracao serializado em config/souphud.json. */
public class HudConfig {
    public boolean enabled = true;
    public boolean showBackground = true;
    public boolean lowSoupAlert = true;
    /** Alerta quando a quantidade de sopas for menor ou igual a este valor. */
    public int lowSoupThreshold = 3;
    public int alertColor = 0xFF5555;

    public ElementSettings soup = ElementType.SOUP.createDefaults();
    public ElementSettings bowl = ElementType.BOWL.createDefaults();
    public ElementSettings freeSlots = ElementType.FREE_SLOTS.createDefaults();
    public ElementSettings status = ElementType.STATUS.createDefaults();

    /** Incrementado a cada mudanca de configuracao; o HUD so reconstroi seus textos quando muda. */
    private transient int revision;

    public int getRevision() {
        return revision;
    }

    public void markChanged() {
        revision++;
    }

    public ElementSettings get(ElementType type) {
        switch (type) {
            case SOUP:
                return soup;
            case BOWL:
                return bowl;
            case FREE_SLOTS:
                return freeSlots;
            case STATUS:
                return status;
            default:
                throw new IllegalArgumentException(String.valueOf(type));
        }
    }

    private void set(ElementType type, ElementSettings value) {
        switch (type) {
            case SOUP:
                soup = value;
                break;
            case BOWL:
                bowl = value;
                break;
            case FREE_SLOTS:
                freeSlots = value;
                break;
            case STATUS:
                status = value;
                break;
            default:
                throw new IllegalArgumentException(String.valueOf(type));
        }
    }

    /** Corrige valores invalidos (arquivo editado a mao, campos ausentes etc.). */
    public void sanitize() {
        for (ElementType type : ElementType.values()) {
            ElementSettings s = get(type);
            if (s == null) {
                s = type.createDefaults();
                set(type, s);
            }
            s.sanitize(type);
        }
        lowSoupThreshold = Math.max(0, Math.min(36, lowSoupThreshold));
        alertColor &= 0xFFFFFF;
    }

    public void resetPositions() {
        for (ElementType type : ElementType.values()) {
            ElementSettings s = get(type);
            s.x = type.getDefaultX();
            s.y = type.getDefaultY();
        }
    }

    public void resetAll() {
        copyFrom(new HudConfig());
    }

    private void copyFrom(HudConfig o) {
        enabled = o.enabled;
        showBackground = o.showBackground;
        lowSoupAlert = o.lowSoupAlert;
        lowSoupThreshold = o.lowSoupThreshold;
        alertColor = o.alertColor;
        soup = o.soup;
        bowl = o.bowl;
        freeSlots = o.freeSlots;
        status = o.status;
        markChanged();
    }
}
