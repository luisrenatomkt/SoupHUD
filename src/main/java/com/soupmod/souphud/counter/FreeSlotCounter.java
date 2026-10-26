package com.soupmod.souphud.counter;

import net.minecraft.item.ItemStack;

/** Conta slots livres do inventario principal (36 slots; armadura nao entra). */
public final class FreeSlotCounter {

    private int free;

    /** @return true se o valor mudou. */
    public boolean scan(ItemStack[] slots) {
        int n = 0;
        for (int i = 0; i < slots.length; i++) {
            ItemStack stack = slots[i];
            if (stack == null || stack.stackSize <= 0) {
                n++;
            }
        }
        boolean changed = n != free;
        free = n;
        return changed;
    }

    public boolean reset() {
        boolean changed = free != 0;
        free = 0;
        return changed;
    }

    public int getFree() {
        return free;
    }
}
