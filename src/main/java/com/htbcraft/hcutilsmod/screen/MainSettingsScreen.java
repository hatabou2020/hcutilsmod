package com.htbcraft.hcutilsmod.screen;

import com.htbcraft.hcutilsmod.common.HCCrypt;
import com.htbcraft.hcutilsmod.common.HCSettings;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class MainSettingsScreen extends SettingsScreen {
    public MainSettingsScreen() {
        super(null, Component.translatable("hcutilsmod.settings.title"));
    }

    protected void init() {
        super.init();

        // プレイヤー座標の表示・非表示
        this.addRenderableWidget(new Button(getPosX() + (getWidth() - 180) / 2, getPosY() + 30, 180, 20,
            getEnableCordsModText(),
            (var1) -> {
                HCSettings.getInstance().enableCordsMod = !HCSettings.getInstance().enableCordsMod;
                var1.setMessage(getEnableCordsModText());
            }));

        // インベントリ設定
        this.addRenderableWidget(new Button(getPosX() + (getWidth() - 180) / 2, getPosY() + 55, 180, 20,
            Component.translatable("hcutilsmod.settings.inventory.title").append("..."),
            (var1) -> this.getMinecraft().setScreen(new InventorySettingsScreen(this))));

        // スポナーの検索設定
        this.addRenderableWidget(new Button(getPosX() + (getWidth() - 180) / 2, getPosY() + 80, 180, 20,
            Component.translatable("hcutilsmod.settings.findspawner.title").append("..."),
            (var1) -> this.getMinecraft().setScreen(new FindSpawnerSettingsScreen(this))));

        // 明るさ表示設定
        this.addRenderableWidget(new Button(getPosX() + (getWidth() - 180) / 2, getPosY() + 105, 180, 20,
            Component.translatable("hcutilsmod.settings.brightness.title").append("..."),
            (var1) -> this.getMinecraft().setScreen(new BrightnessSettingsScreen(this))));

        if (HCCrypt.isSupportOS()) {
            // Twitter設定
            this.addRenderableWidget(new Button(getPosX() + (getWidth() - 180) / 2, getPosY() + 130, 180, 20,
                Component.translatable("hcutilsmod.settings.twitter.title").append("..."),
                (var1) -> this.getMinecraft().setScreen(new TwitterSettingsScreen(this))));
        }

        // ゲームに戻る
        this.addRenderableWidget(new Button(getPosX() + (getWidth() - 100) / 2, getPosY() + 165, 100, 20,
            Component.translatable("hcutilsmod.settings.return"),
            (var1) -> this.getMinecraft().setScreen(null)));
    }

    private Component getEnableCordsModText() {
        if (HCSettings.getInstance().enableCordsMod) {
            return Component.translatable("hcutilsmod.settings.cords.enable");
        }
        else {
            return Component.translatable("hcutilsmod.settings.cords.disable");
        }
    }
}
