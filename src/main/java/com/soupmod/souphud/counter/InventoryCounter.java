package com.soupmod.souphud.counter;

import com.soupmod.souphud.config.CountMode;
import net.minecraft.item.ItemStack;

/**
 * Base dos contadores. Percorre os slots reais (sem alocar objetos) e guarda a contagem da hotbar
 * (slots 0-8) e do inventario principal (slots 0-35) de uma so vez.
 */
public abstract class InventoryCounter {

    public static final int HOTBAR_SIZE = 9;

    private int hotbarCount;
    private int inventoryCount;

    protected abstract boolean matches(ItemStack stack);

    /** @return true se algum valor mudou. */
    public final boolean scan(ItemStack[] slots) {
        int hotbar = 0;
        int total = 0;
        for (int i = 0; i < slots.length; i++) {
            ItemStack stack = slots[i];
            if (stack == null || stack.stackSize <= 0 || !matches(stack)) {
                continue;
            }
            total += stack.stackSize;
            if (i < HOTBAR_SIZE) {
                hotbar += stack.stackSize;
            }
        }
        boolean changed = hotbar != hotbarCount || total != inventoryCount;
        hotbarCount = hotbar;
        inventoryCount = total;
        return changed;
    }

    public final boolean reset() {
        boolean changed = hotbarCount != 0 || inventoryCount != 0;
        hotbarCount = 0;
        inventoryCount = 0;
        return changed;
    }

    public final int get(CountMode mode) {
        return mode == CountMode.HOTBAR ? hotbarCount : inventoryCount;
    }
}
