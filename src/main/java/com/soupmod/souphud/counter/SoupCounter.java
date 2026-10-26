package com.soupmod.souphud.counter;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

/** Conta sopas de cogumelo (mushroom_stew), usadas no SoupPvP. */
public final class SoupCounter extends InventoryCounter {
    @Override
    protected boolean matches(ItemStack stack) {
        return stack.getItem() == Items.mushroom_stew;
    }
}
