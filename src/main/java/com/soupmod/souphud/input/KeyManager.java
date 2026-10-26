package com.soupmod.souphud.input;

import com.soupmod.souphud.gui.HudEditorScreen;
import com.soupmod.souphud.gui.SettingsScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

/**
 * Teclas do mod. Ficam em Opcoes > Controles > "Soup HUD", onde o jogador pode trocar.
 * Padroes: Shift direito = configuracoes; Ctrl direito = editor de HUD.
 */
public final class KeyManager {

    private static final String CATEGORY = "Soup HUD";

    private final KeyBinding openSettings =
            new KeyBinding("Abrir configura\u00e7\u00f5es", Keyboard.KEY_RSHIFT, CATEGORY);
    private final KeyBinding openEditor =
            new KeyBinding("Abrir editor de HUD", Keyboard.KEY_RCONTROL, CATEGORY);

    public void register() {
        ClientRegistry.registerKeyBinding(openSettings);
        ClientRegistry.registerKeyBinding(openEditor);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        // isPressed() consome o clique da tecla; chamamos sempre para nao acumular cliques pendentes.
        boolean settingsPressed = openSettings.isPressed();
        boolean editorPressed = openEditor.isPressed();

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.currentScreen != null) {
            return;
        }
        if (settingsPressed) {
            mc.displayGuiScreen(new SettingsScreen());
        } else if (editorPressed) {
            mc.displayGuiScreen(new HudEditorScreen(null));
        }
    }
}
