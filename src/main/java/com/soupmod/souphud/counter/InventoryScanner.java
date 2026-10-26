package com.soupmod.souphud.counter;

import com.soupmod.souphud.config.CountMode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemStack;

/**
 * Atualiza os contadores uma vez por tick (20x/s) lendo os slots reais do jogador.
 * Somente leitura: nunca move, consome ou troca itens.
 */
public final class InventoryScanner {

    private final SoupCounter soup = new SoupCounter();
    private final BowlCounter bowl = new BowlCounter();
    private final FreeSlotCounter freeSlots = new FreeSlotCounter();
    private int revision;

    public void update(Minecraft mc) {
        EntityPlayerSP player = mc.thePlayer;
        boolean changed;
        if (player == null || player.inventory == null) {
            changed = soup.reset();
            changed |= bowl.reset();
            changed |= freeSlots.reset();
        } else {
            ItemStack[] slots = player.inventory.mainInventory;
            changed = soup.scan(slots);
            changed |= bowl.scan(slots);
            changed |= freeSlots.scan(slots);
        }
        if (changed) {
            revision++;
        }
    }

    public int getSoups(CountMode mode) {
        return soup.get(mode);
    }

    public int getBowls(CountMode mode) {
        return bowl.get(mode);
    }

    public int getFreeSlots() {
        return freeSlots.getFree();
    }

    public boolean isInventoryFull() {
        return freeSlots.getFree() == 0;
    }

    /** Muda sempre que alguma contagem muda. */
    public int getRevision() {
        return revision;
    }
}
