package com.soupmod.souphud.event;

import com.soupmod.souphud.counter.InventoryScanner;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/** Atualiza as contagens uma vez por tick, ao final dele (depois dos pacotes e cliques do tick). */
public final class ClientTickHandler {

    private final InventoryScanner scanner;

    public ClientTickHandler(InventoryScanner scanner) {
        this.scanner = scanner;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        scanner.update(Minecraft.getMinecraft());
    }
}
