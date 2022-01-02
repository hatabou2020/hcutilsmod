package com.htbcraft.hcutilsmod.screen;

import com.htbcraft.hcutilsmod.common.HCSettings;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TranslatableComponent;

public class MainSettingsScreen extends SettingsScreen {
    public MainSettingsScreen() {
        super(null, new TranslatableComponent("hcutilsmod.settings.title"));
    }

    protected void init() {
        super.init();

        // プレイヤー座標の表示・非表示
        this.addRenderableWidget(new Button(getPosX() + (getWidth() - 180) / 2, getPosY() + 30, 180, 20,
                getEnableCordsModText(), (var1) -> {
            HCSettings.getInstance().enableCordsMod = !HCSettings.getInstance().enableCordsMod;
            var1.setMessage(getEnableCordsModText());
        }));

        // インベントリのソート種類
        this.addRenderableWidget(new Button(getPosX() + (getWidth() - 180) / 2, getPosY() + 55, 180, 20,
                getInventorySortTypeText(), (var1) -> {
            HCSettings.SortType[] values = HCSettings.SortType.values();
            int sortType = HCSettings.getInstance().sortType.ordinal();
            if (++sortType >= values.length) {
                sortType = 0;
            }
            HCSettings.getInstance().sortType = values[sortType];
            var1.setMessage(getInventorySortTypeText());
        }));

        // スポナーの検索設定
        this.addRenderableWidget(new Button(getPosX() + (getWidth() - 180) / 2, getPosY() + 80, 180, 20,
                new TranslatableComponent("hcutilsmod.settings.findspawner.title").append("..."),
                (var1) -> this.getMinecraft().setScreen(new FindSpawnerSettingsScreen(this))));

        // 明るさ表示設定
        this.addRenderableWidget(new Button(getPosX() + (getWidth() - 180) / 2, getPosY() + 105, 180, 20,
                new TranslatableComponent("hcutilsmod.settings.brightness.title").append("..."),
                (var1) -> this.getMinecraft().setScreen(new BrightnessSettingsScreen(this))));

        // ゲームに戻る
        this.addRenderableWidget(new Button(getPosX() + (getWidth() - 100) / 2, getPosY() + 140, 100, 20,
                        new TranslatableComponent("hcutilsmod.settings.return"),
                        (var1) -> this.getMinecraft().setScreen(null)));
    }

    private TranslatableComponent getEnableCordsModText() {
        if (HCSettings.getInstance().enableCordsMod) {
            return new TranslatableComponent("hcutilsmod.settings.cords.enable");
        }
        else {
            return new TranslatableComponent("hcutilsmod.settings.cords.disable");
        }
    }

    private TranslatableComponent getInventorySortTypeText() {
        if (HCSettings.getInstance().sortType == HCSettings.SortType.CATEGORY) {
            return new TranslatableComponent("hcutilsmod.settings.inventory.sorttype.category");
        }
        else {
            return new TranslatableComponent("hcutilsmod.settings.inventory.sorttype.name");
        }
    }
}
