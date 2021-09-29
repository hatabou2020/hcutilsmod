package com.htbcraft.hcutilsmod.mods.inventory;

import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Comparator;

public class InventoryNameSort implements Comparator<ItemStack> {
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public int compare(ItemStack o1, ItemStack o2) {
        String s1 = o1.getDisplayName().getString();
        String s2 = o2.getDisplayName().getString();
        LOGGER.info(s1 + " / " + s2);

        // AIRは後ろに回す
        if ((o1.isEmpty()) && (o2.isEmpty())) {
            return 0;
        }
        else if (o1.isEmpty()) {
            return 1;
        }
        else if (o2.isEmpty()) {
            return -1;
        }

        // 同じならスタック数の多い方を前に
        if (o1.sameItem(o2)) {
            return o2.getCount() - o1.getCount();
        }

        return s1.compareTo(s2);
    }
}
