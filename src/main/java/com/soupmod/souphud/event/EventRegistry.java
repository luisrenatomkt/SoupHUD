package com.soupmod.souphud.event;

import com.soupmod.souphud.counter.InventoryScanner;
import com.soupmod.souphud.hud.HudManager;
import com.soupmod.souphud.input.KeyManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;

/**
 * Registro centralizado dos eventos do Forge 1.8.9.
 * Eventos de tick/teclado passam pelo barramento do FML; eventos de render pelo MinecraftForge.EVENT_BUS.
 */
public final class EventRegistry {

    private EventRegistry() {
    }

    public static void registerAll(KeyManager keys, HudManager hud, InventoryScanner scanner) {
        keys.register();
        FMLCommonHandler.instance().bus().register(keys);
        FMLCommonHandler.instance().bus().register(new ClientTickHandler(scanner));
        MinecraftForge.EVENT_BUS.register(new RenderHandler(hud));
    }
}
