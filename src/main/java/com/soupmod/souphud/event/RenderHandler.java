package com.soupmod.souphud.event;

import com.soupmod.souphud.gui.HudScreen;
import com.soupmod.souphud.hud.HudManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/** Desenha o HUD ao final do overlay do jogo (nao cancela nem altera nenhum elemento vanilla). */
public final class RenderHandler {

    private final HudManager hud;

    public RenderHandler(HudManager hud) {
        this.hud = hud;
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.ALL) {
            return;
        }
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.gameSettings.hideGUI) {
            return;
        }
        // Editor e configuracoes desenham o HUD por conta propria (evita desenhar duas vezes).
        if (mc.currentScreen instanceof HudScreen) {
            return;
        }
        hud.renderOverlay(event.resolution.getScaledWidth(), event.resolution.getScaledHeight());
    }
}
