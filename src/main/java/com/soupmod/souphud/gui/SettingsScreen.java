package com.soupmod.souphud.gui;

import com.soupmod.souphud.SoupHudMod;
import com.soupmod.souphud.config.ElementSettings;
import com.soupmod.souphud.config.ElementType;
import com.soupmod.souphud.config.HudConfig;
import com.soupmod.souphud.hud.HudElement;
import com.soupmod.souphud.hud.HudLayout;
import com.soupmod.souphud.hud.HudManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

/**
 * Menu de configuracoes. O fundo e transparente de proposito: o HUD aparece ao vivo atras dos botoes
 * (previa em tempo real). Tudo e gravado em disco ao fechar a tela.
 */
public class SettingsScreen extends GuiScreen implements HudScreen {

    private static final int ID_ENABLED = 1;
    private static final int ID_BACKGROUND = 2;
    private static final int ID_ALERT = 3;
    private static final int ID_EDITOR = 4;
    private static final int ID_RESET = 5;
    private static final int ID_DONE = 6;
    private static final int ID_SELECT = 10;
    private static final int ID_ELEM_ENABLED = 11;
    private static final int ID_MODE = 12;
    private static final int ID_EXTRA_A = 13;
    private static final int ID_EXTRA_B = 14;
    private static final int ID_COLOR = 15;

    private static final int ROW = 22;

    private final HudConfig cfg;
    private final HudManager hud;
    private ElementType selected = ElementType.SOUP;
    private boolean resetArmed;

    private GuiButton btnEnabled;
    private GuiButton btnBackground;
    private GuiButton btnAlert;
    private GuiButton btnEditor;
    private GuiButton btnReset;
    private GuiButton btnDone;
    private SliderButton sliderThreshold;

    private GuiButton btnSelect;
    private GuiButton btnElemEnabled;
    private GuiButton btnMode;
    private GuiButton btnExtraA;
    private GuiButton btnExtraB;
    private GuiButton btnColor;
    private SliderButton sliderScale;
    private SliderButton sliderX;
    private SliderButton sliderY;
    private SliderButton sliderOpacity;

    private int panelW;
    private int colW;
    private int half;
    private int leftX;
    private int top;

    public SettingsScreen() {
        this.cfg = SoupHudMod.getConfigManager().getConfig();
        this.hud = SoupHudMod.getHudManager();
    }

    private <T extends GuiButton> T addBtn(T button) {
        this.buttonList.add(button);
        return button;
    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        panelW = Math.min(340, this.width - 8);
        colW = (panelW - 10) / 2;
        half = (colW - 4) / 2;
        leftX = this.width / 2 - panelW / 2;
        int rightX = leftX + colW + 10;
        top = Math.max(34, this.height / 2 - 84);

        // Coluna esquerda: opcoes globais
        btnEnabled = addBtn(new GuiButton(ID_ENABLED, leftX, top, colW, 20, ""));
        btnBackground = addBtn(new GuiButton(ID_BACKGROUND, leftX, top + ROW, colW, 20, ""));
        btnAlert = addBtn(new GuiButton(ID_ALERT, leftX, top + 2 * ROW, colW, 20, ""));
        sliderThreshold = addBtn(new SliderButton(ID_ALERT + 100, leftX, top + 3 * ROW, colW, 20,
                0.0F, 20.0F, 1.0F, cfg.lowSoupThreshold,
                v -> "Alerta a partir de: " + Math.round(v),
                v -> {
                    cfg.lowSoupThreshold = Math.round(v);
                    cfg.markChanged();
                }));
        btnEditor = addBtn(new GuiButton(ID_EDITOR, leftX, top + 4 * ROW, colW, 20, "Abrir editor de HUD"));
        btnReset = addBtn(new GuiButton(ID_RESET, leftX, top + 5 * ROW, colW, 20, ""));
        btnDone = addBtn(new GuiButton(ID_DONE, leftX, top + 6 * ROW, colW, 20, "Concluir"));

        // Coluna direita: elemento selecionado
        btnSelect = addBtn(new GuiButton(ID_SELECT, rightX, top, colW, 20, ""));
        btnElemEnabled = addBtn(new GuiButton(ID_ELEM_ENABLED, rightX, top + ROW, half, 20, ""));
        btnMode = addBtn(new GuiButton(ID_MODE, rightX + half + 4, top + ROW, half, 20, ""));
        btnExtraA = addBtn(new GuiButton(ID_EXTRA_A, rightX, top + 2 * ROW, colW, 20, ""));
        btnExtraB = addBtn(new GuiButton(ID_EXTRA_B, rightX + half + 4, top + 2 * ROW, half, 20, ""));
        btnColor = addBtn(new GuiButton(ID_COLOR, rightX, top + 3 * ROW, half, 20, ""));
        sliderScale = addBtn(new SliderButton(ID_COLOR + 100, rightX + half + 4, top + 3 * ROW, half, 20,
                0.5F, 3.0F, 0.05F, 1.0F,
                v -> "Escala: " + String.format("%.2f", v),
                v -> cfg.get(selected).scale = v));
        sliderX = addBtn(new SliderButton(ID_COLOR + 101, rightX, top + 4 * ROW, half, 20,
                0.0F, 100.0F, 1.0F, 0.0F,
                v -> "X: " + Math.round(v) + "%",
                v -> cfg.get(selected).x = v / 100.0F));
        sliderY = addBtn(new SliderButton(ID_COLOR + 102, rightX + half + 4, top + 4 * ROW, half, 20,
                0.0F, 100.0F, 1.0F, 0.0F,
                v -> "Y: " + Math.round(v) + "%",
                v -> cfg.get(selected).y = v / 100.0F));
        sliderOpacity = addBtn(new SliderButton(ID_COLOR + 103, rightX, top + 5 * ROW, colW, 20,
                10.0F, 100.0F, 5.0F, 100.0F,
                v -> "Transpar\u00eancia: " + Math.round(v) + "%",
                v -> cfg.get(selected).opacity = v / 100.0F));

        syncControls();
    }

    private static String onOff(boolean value) {
        return value ? "ON" : "OFF";
    }

    private void syncControls() {
        ElementSettings s = cfg.get(selected);

        btnEnabled.displayString = "Soup HUD: " + onOff(cfg.enabled);
        btnBackground.displayString = "Fundo: " + onOff(cfg.showBackground);
        btnAlert.displayString = "Alerta de sopas baixas: " + onOff(cfg.lowSoupAlert);
        sliderThreshold.setValue(cfg.lowSoupThreshold);
        sliderThreshold.enabled = cfg.lowSoupAlert;
        btnReset.displayString = resetArmed ? "Clique de novo para confirmar" : "Restaurar tudo";

        btnSelect.displayString = "Elemento: " + selected.getDisplayName();
        btnElemEnabled.displayString = "Ativo: " + onOff(s.enabled);
        btnMode.displayString = "Modo: " + s.displayMode.getLabel();
        btnColor.displayString = "Cor: " + ColorPresets.nameOf(s.color);

        switch (selected) {
            case SOUP:
            case BOWL:
                btnExtraA.visible = true;
                btnExtraA.width = colW;
                btnExtraA.displayString = "Contar: " + s.countMode.getLabel();
                btnExtraB.visible = false;
                break;
            case STATUS:
                btnExtraA.visible = true;
                btnExtraA.width = half;
                btnExtraA.displayString = "Sopas: " + onOff(s.showSoups);
                btnExtraB.visible = true;
                btnExtraB.displayString = "Tigelas: " + onOff(s.showBowls);
                break;
            default:
                btnExtraA.visible = false;
                btnExtraB.visible = false;
                break;
        }

        sliderScale.setValue(s.scale);
        sliderX.setValue(s.x * 100.0F);
        sliderY.setValue(s.y * 100.0F);
        sliderOpacity.setValue(s.opacity * 100.0F);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        ElementSettings s = cfg.get(selected);
        boolean wasArmed = resetArmed;
        resetArmed = false;

        switch (button.id) {
            case ID_ENABLED:
                cfg.enabled = !cfg.enabled;
                break;
            case ID_BACKGROUND:
                cfg.showBackground = !cfg.showBackground;
                break;
            case ID_ALERT:
                cfg.lowSoupAlert = !cfg.lowSoupAlert;
                break;
            case ID_EDITOR:
                this.mc.displayGuiScreen(new HudEditorScreen(this));
                return;
            case ID_RESET:
                if (wasArmed) {
                    cfg.resetAll();
                } else {
                    resetArmed = true;
                }
                break;
            case ID_DONE:
                this.mc.displayGuiScreen(null);
                return;
            case ID_SELECT:
                selected = selected.next();
                break;
            case ID_ELEM_ENABLED:
                s.enabled = !s.enabled;
                break;
            case ID_MODE:
                s.displayMode = s.displayMode.next();
                break;
            case ID_EXTRA_A:
                if (selected == ElementType.STATUS) {
                    s.showSoups = !s.showSoups;
                } else {
                    s.countMode = s.countMode.next();
                }
                break;
            case ID_EXTRA_B:
                s.showBowls = !s.showBowls;
                break;
            case ID_COLOR:
                s.color = ColorPresets.next(s.color);
                break;
            default:
                return;
        }
        cfg.markChanged();
        syncControls();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawRect(0, 0, this.width, this.height, 0x44000000);
        drawRect(leftX - 6, top - 24, leftX + panelW + 6, top + 7 * ROW + 2, 0xAA101010);
        drawCenteredString(this.fontRendererObj, "Soup HUD - Configura\u00e7\u00f5es", this.width / 2, top - 16, 0xFFFFFF);

        // Previa ao vivo: o HUD real, mais o elemento selecionado destacado (mesmo se estiver desativado).
        hud.renderOverlay(this.width, this.height);
        HudElement element = hud.getElement(selected);
        if (element != null && element.hasContent()) {
            ElementSettings s = cfg.get(selected);
            if (!s.enabled || !cfg.enabled) {
                hud.getRenderer().draw(element, s, cfg, this.width, this.height, 0.45F);
            }
            int wPx = Math.round(element.getWidth() * s.scale);
            int hPx = Math.round(element.getHeight() * s.scale);
            hud.getRenderer().drawOutline(HudLayout.resolveX(s, this.width, wPx),
                    HudLayout.resolveY(s, this.height, hPx), wPx, hPx, 0xAAFFFF55);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);

        // Amostra da cor atual dentro do botao de cor
        drawRect(btnColor.xPosition + btnColor.width - 14, btnColor.yPosition + 6,
                btnColor.xPosition + btnColor.width - 6, btnColor.yPosition + 14,
                0xFF000000 | cfg.get(selected).color);
    }

    @Override
    public void onGuiClosed() {
        SoupHudMod.getConfigManager().save();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
