package com.soupmod.souphud.gui;

import com.soupmod.souphud.SoupHudMod;
import com.soupmod.souphud.config.ElementSettings;
import com.soupmod.souphud.config.ElementType;
import com.soupmod.souphud.config.HudConfig;
import com.soupmod.souphud.hud.HudElement;
import com.soupmod.souphud.hud.HudLayout;
import com.soupmod.souphud.hud.HudManager;
import com.soupmod.souphud.hud.HudRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

/**
 * Editor visual: arraste os elementos com o botao esquerdo. So existe enquanto esta tela esta aberta;
 * em jogo normal o HUD nunca reage ao mouse. ESC (ou "Salvar e sair") grava; "Cancelar" desfaz.
 */
public class HudEditorScreen extends GuiScreen implements HudScreen {

    private static final int BTN_SAVE = 0;
    private static final int BTN_RESET = 1;
    private static final int BTN_CANCEL = 2;
    private static final int SNAP_DISTANCE = 3;

    private final GuiScreen parent;
    private final HudConfig cfg;
    private final HudManager hud;
    private final float[] startX;
    private final float[] startY;

    private HudElement dragging;
    private float grabX;
    private float grabY;
    private boolean snapVertical;
    private boolean snapHorizontal;

    /** @param parent tela para voltar ao fechar (null = voltar ao jogo). */
    public HudEditorScreen(GuiScreen parent) {
        this.parent = parent;
        this.cfg = SoupHudMod.getConfigManager().getConfig();
        this.hud = SoupHudMod.getHudManager();
        ElementType[] types = ElementType.values();
        this.startX = new float[types.length];
        this.startY = new float[types.length];
        for (int i = 0; i < types.length; i++) {
            ElementSettings s = cfg.get(types[i]);
            startX[i] = s.x;
            startY[i] = s.y;
        }
    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        int bw = 100;
        int gap = 5;
        int total = bw * 3 + gap * 2;
        int x0 = this.width / 2 - total / 2;
        int y = this.height - 26;
        this.buttonList.add(new GuiButton(BTN_SAVE, x0, y, bw, 20, "Salvar e sair"));
        this.buttonList.add(new GuiButton(BTN_RESET, x0 + bw + gap, y, bw, 20, "Restaurar padr\u00e3o"));
        this.buttonList.add(new GuiButton(BTN_CANCEL, x0 + 2 * (bw + gap), y, bw, 20, "Cancelar"));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case BTN_SAVE:
                close(false);
                break;
            case BTN_RESET:
                cfg.resetPositions();
                break;
            case BTN_CANCEL:
                close(true);
                break;
            default:
                break;
        }
    }

    private void close(boolean revert) {
        if (revert) {
            ElementType[] types = ElementType.values();
            for (int i = 0; i < types.length; i++) {
                ElementSettings s = cfg.get(types[i]);
                s.x = startX[i];
                s.y = startY[i];
            }
        }
        this.mc.displayGuiScreen(parent);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            close(false);
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }

    private HudElement pick(int mx, int my) {
        HudElement[] elements = hud.getElements();
        for (int i = elements.length - 1; i >= 0; i--) {
            HudElement e = elements[i];
            ElementSettings s = cfg.get(e.getType());
            float wPx = e.getWidth() * s.scale;
            float hPx = e.getHeight() * s.scale;
            int px = HudLayout.resolveX(s, this.width, wPx);
            int py = HudLayout.resolveY(s, this.height, hPx);
            if (mx >= px && mx < px + wPx && my >= py && my < py + hPx) {
                return e;
            }
        }
        return null;
    }

    private boolean overButton(int mx, int my) {
        for (int i = 0; i < this.buttonList.size(); i++) {
            GuiButton b = this.buttonList.get(i);
            if (b.visible && mx >= b.xPosition && mx < b.xPosition + b.width
                    && my >= b.yPosition && my < b.yPosition + b.height) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseButton != 0 || overButton(mouseX, mouseY)) {
            return;
        }
        hud.ensureFresh();
        HudElement hit = pick(mouseX, mouseY);
        if (hit != null) {
            ElementSettings s = cfg.get(hit.getType());
            int px = HudLayout.resolveX(s, this.width, hit.getWidth() * s.scale);
            int py = HudLayout.resolveY(s, this.height, hit.getHeight() * s.scale);
            dragging = hit;
            grabX = mouseX - px;
            grabY = mouseY - py;
        }
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        if (dragging == null || clickedMouseButton != 0) {
            return;
        }
        ElementSettings s = cfg.get(dragging.getType());
        float wPx = dragging.getWidth() * s.scale;
        float hPx = dragging.getHeight() * s.scale;
        float nx = clamp(mouseX - grabX, 0.0F, Math.max(0.0F, this.width - wPx));
        float ny = clamp(mouseY - grabY, 0.0F, Math.max(0.0F, this.height - hPx));

        snapVertical = false;
        snapHorizontal = false;
        if (Math.abs(nx + wPx / 2.0F - this.width / 2.0F) <= SNAP_DISTANCE) {
            nx = this.width / 2.0F - wPx / 2.0F;
            snapVertical = true;
        }
        if (Math.abs(ny + hPx / 2.0F - this.height / 2.0F) <= SNAP_DISTANCE) {
            ny = this.height / 2.0F - hPx / 2.0F;
            snapHorizontal = true;
        }
        s.x = nx / this.width;
        s.y = ny / this.height;
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        dragging = null;
        snapVertical = false;
        snapHorizontal = false;
    }

    private static float clamp(float v, float lo, float hi) {
        return v < lo ? lo : (v > hi ? hi : v);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawRect(0, 0, this.width, this.height, 0x66000000);
        if (snapVertical) {
            drawRect(this.width / 2, 0, this.width / 2 + 1, this.height, 0x99FFFFFF);
        }
        if (snapHorizontal) {
            drawRect(0, this.height / 2, this.width, this.height / 2 + 1, 0x99FFFFFF);
        }

        hud.ensureFresh();
        HudRenderer renderer = hud.getRenderer();
        HudElement hovered = dragging != null ? dragging : pick(mouseX, mouseY);
        HudElement[] elements = hud.getElements();

        for (int i = 0; i < elements.length; i++) {
            HudElement e = elements[i];
            ElementSettings s = cfg.get(e.getType());
            boolean active = cfg.enabled && s.enabled;
            if (e.hasContent()) {
                renderer.draw(e, s, cfg, this.width, this.height, active ? 1.0F : 0.45F);
            }
            int wPx = Math.round(e.getWidth() * s.scale);
            int hPx = Math.round(e.getHeight() * s.scale);
            int px = HudLayout.resolveX(s, this.width, wPx);
            int py = HudLayout.resolveY(s, this.height, hPx);

            int outline = 0x66FFFFFF;
            if (e == dragging) {
                outline = 0xFFFFFFFF;
            } else if (e == hovered) {
                outline = 0xCCFFFF55;
            }
            renderer.drawOutline(px, py, wPx, hPx, outline);

            if (e == hovered) {
                String label = e.getType().getDisplayName() + (active ? "" : " (desativado)");
                int ly = py >= 11 ? py - 10 : py + hPx + 3;
                this.fontRendererObj.drawStringWithShadow(label, (float) px, (float) ly, 0xFFFFFF);
            }
        }

        drawCenteredString(this.fontRendererObj, "Editor de HUD", this.width / 2, 8, 0xFFFFFF);
        drawCenteredString(this.fontRendererObj,
                "Arraste os elementos com o mouse. ESC salva e sai.", this.width / 2, 20, 0xAAAAAA);

        super.drawScreen(mouseX, mouseY, partialTicks);
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
