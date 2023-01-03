package com.htbcraft.hcutilsmod.screen;

import com.htbcraft.hcutilsmod.common.HCSettings;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class InventorySettingsScreen extends SettingsScreen {
    public InventorySettingsScreen(Screen parent) {
        super(parent, Component.translatable("hcutilsmod.settings.inventory.title"));
    }

    protected void init() {
        super.init();

        // インベントリのソート種類
        this.addRenderableWidget(
                Button.builder(
                    getInventorySortTypeText(),
                    (var1) -> {
                        HCSettings.SortType[] values = HCSettings.SortType.values();
                        int sortType = HCSettings.getInstance().sortType.ordinal();
                        if (++sortType >= values.length) {
                            sortType = 0;
                        }
                        HCSettings.getInstance().sortType = values[sortType];
                        var1.setMessage(getInventorySortTypeText());
                    })
                .pos(getPosX() + (getWidth() - 180) / 2, getPosY() + 30)
                .size(180, 20)
                .build());

        // オンオフ
        this.addRenderableWidget(
                Button.builder(
                    getEnableAutoReplaceItemText(),
                    (var1) -> {
                        HCSettings.getInstance().enableAutoReplaceItem = !HCSettings.getInstance().enableAutoReplaceItem;
                        var1.setMessage(getEnableAutoReplaceItemText());
                    })
                .pos(getPosX() + (getWidth() - 180) / 2, getPosY() + 55)
                .size(180, 20)
                .build());

        // 戻る
        this.addRenderableWidget(
                Button.builder(
                    Component.translatable("hcutilsmod.settings.inventory.return"),
                    (var1) -> this.getMinecraft().setScreen(this.getParent()))
                .pos(getPosX() + (getWidth() - 100) / 2, getPosY() + 140)
                .size(100, 20)
                .build());
    }

    private Component getInventorySortTypeText() {
        if (HCSettings.getInstance().sortType == HCSettings.SortType.CATEGORY) {
            return Component.translatable("hcutilsmod.settings.inventory.sorttype.category");
        }
        else {
            return Component.translatable("hcutilsmod.settings.inventory.sorttype.name");
        }
    }

    private Component getEnableAutoReplaceItemText() {
        if (HCSettings.getInstance().enableAutoReplaceItem) {
            return Component.translatable("hcutilsmod.settings.inventory.autoreplaceitem.enable");
        }
        else {
            return Component.translatable("hcutilsmod.settings.inventory.autoreplaceitem.disable");
        }
    }

    public boolean shouldCloseOnEsc() {
        return false;
    }   // この画面はESCをはじく
}
