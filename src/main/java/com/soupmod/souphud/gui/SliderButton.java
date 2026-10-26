package com.soupmod.souphud.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

/** Slider simples baseado em GuiButton (o GuiOptionSlider vanilla so serve para opcoes do jogo). */
public class SliderButton extends GuiButton {

    public interface Formatter {
        String format(float value);
    }

    public interface Listener {
        void onChanged(float value);
    }

    private final float min;
    private final float max;
    private final float step;
    private final Formatter formatter;
    private final Listener listener;
    private float value;
    private boolean dragging;

    public SliderButton(int id, int x, int y, int width, int height, float min, float max, float step,
                        float value, Formatter formatter, Listener listener) {
        super(id, x, y, width, height, "");
        this.min = min;
        this.max = max;
        this.step = step;
        this.formatter = formatter;
        this.listener = listener;
        setValue(value);
    }

    public void setValue(float v) {
        this.value = snap(v);
        this.displayString = formatter.format(this.value);
    }

    public float getValue() {
        return value;
    }

    private float snap(float v) {
        if (v < min) {
            v = min;
        }
        if (v > max) {
            v = max;
        }
        if (step > 0.0F) {
            v = Math.round((v - min) / step) * step + min;
            if (v > max) {
                v = max;
            }
        }
        return v;
    }

    private float ratio() {
        return max > min ? (value - min) / (max - min) : 0.0F;
    }

    private void updateFromMouse(int mouseX) {
        float r = (float) (mouseX - (this.xPosition + 4)) / (float) (this.width - 8);
        if (r < 0.0F) {
            r = 0.0F;
        }
        if (r > 1.0F) {
            r = 1.0F;
        }
        float nv = snap(min + r * (max - min));
        if (nv != value) {
            value = nv;
            displayString = formatter.format(value);
            listener.onChanged(value);
        }
    }

    @Override
    protected int getHoverState(boolean mouseOver) {
        return 0;
    }

    @Override
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
        if (!this.visible) {
            return;
        }
        if (dragging) {
            updateFromMouse(mouseX);
        }
        mc.getTextureManager().bindTexture(buttonTextures);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        int handleX = this.xPosition + (int) (ratio() * (float) (this.width - 8));
        this.drawTexturedModalRect(handleX, this.yPosition, 0, 66, 4, 20);
        this.drawTexturedModalRect(handleX + 4, this.yPosition, 196, 66, 4, 20);
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (super.mousePressed(mc, mouseX, mouseY)) {
            dragging = true;
            updateFromMouse(mouseX);
            return true;
        }
        return false;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        dragging = false;
    }
}
