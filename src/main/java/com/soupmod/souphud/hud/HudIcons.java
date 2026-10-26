package com.soupmod.souphud.hud;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

/** ItemStacks usados como icones. Criados uma unica vez (sem alocacao por frame). */
public final class HudIcons {

    private static ItemStack soup;
    private static ItemStack bowl;
    private static ItemStack chest;

    private HudIcons() {
    }

    public static ItemStack soup() {
        if (soup == null) {
            soup = new ItemStack(Items.mushroom_stew);
        }
        return soup;
    }

    public static ItemStack bowl() {
        if (bowl == null) {
            bowl = new ItemStack(Items.bowl);
        }
        return bowl;
    }

    public static ItemStack chest() {
        if (chest == null) {
            chest = new ItemStack(Blocks.chest);
        }
        return chest;
    }
}
