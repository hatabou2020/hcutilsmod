package com.htbcraft.hcutilsmod.mods.inventory;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Comparator;

public class InventoryCategorySort implements Comparator<ItemStack> {
    private static final Logger LOGGER = LogManager.getLogger();
    private final NonNullList<ItemStack> items = NonNullList.create();

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

        int ret = 0;

        /* クリエイティブモードのカテゴリ順でソートする
         * 1.建築ブロック
         * 2.装飾ブロック
         * 3.レッドストーン
         * 4.運送
         * 5.その他
         * 6.食料
         * 7.道具
         * 8.戦闘
         * 9.醸造
         */
        for (CreativeModeTab tab : CreativeModeTab.TABS) {
            // 余分なタブは省く
            if ((tab == CreativeModeTab.TAB_SEARCH) ||
                (tab == CreativeModeTab.TAB_HOTBAR) ||
                (tab == CreativeModeTab.TAB_INVENTORY)) {
                continue;
            }

            items.clear();
            tab.fillItemList(items);

            for (ItemStack item : items) {
                if (o1.sameItem(item)) {
                    ret = -1;
                    break;
                }
                else if (o2.sameItem(item)) {
                    ret = 1;
                    break;
                }
            }

            if (ret != 0) {
                break;
            }
        }

        return ret;
    }
}
