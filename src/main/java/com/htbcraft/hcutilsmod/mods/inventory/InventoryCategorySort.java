package com.htbcraft.hcutilsmod.mods.inventory;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class InventoryCategorySort implements Comparator<ItemStack> {
    private static final Logger LOGGER = LogManager.getLogger();

    /* クリエイティブモードのカテゴリ順でソートする
     *  1.建築ブロック
     *  2.色付きブロック
     *  3.天然ブロック
     *  4.機能的ブロック
     *  5.レッドストーン
     *  6.道具と実用品
     *  7.戦闘
     *  8.食べ物と飲み物
     *  9.材料
     * 10.スポーンエッグ
     */
    private static final List<CreativeModeTab> TABS = Arrays.asList(
        CreativeModeTabs.BUILDING_BLOCKS,
        CreativeModeTabs.COLORED_BLOCKS,
        CreativeModeTabs.NATURAL_BLOCKS,
        CreativeModeTabs.FUNCTIONAL_BLOCKS,
        CreativeModeTabs.REDSTONE_BLOCKS,
        CreativeModeTabs.TOOLS_AND_UTILITIES,
        CreativeModeTabs.COMBAT,
        CreativeModeTabs.FOOD_AND_DRINKS,
        CreativeModeTabs.INGREDIENTS,
        CreativeModeTabs.SPAWN_EGGS
    );

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

        for (CreativeModeTab tab : TABS) {
            for (ItemStack item : tab.getDisplayItems()) {
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
