package com.soupmod.souphud.counter;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

/** Conta tigelas vazias. */
public final class BowlCounter extends InventoryCounter {
    @Override
    protected boolean matches(ItemStack stack) {
        return stack.getItem() == Items.bowl;
    }
}
