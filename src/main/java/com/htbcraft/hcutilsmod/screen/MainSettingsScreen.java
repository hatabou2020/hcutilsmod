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
        this.addRenderableWidget(
            Button.builder(
                getEnableCordsModText(),
                (var1) -> {
                    HCSettings.getInstance().enableCordsMod = !HCSettings.getInstance().enableCordsMod;
                    var1.setMessage(getEnableCordsModText());
                })
            .pos(getPosX() + (getWidth() - 180) / 2, getPosY() + 30)
            .size(180, 20)
            .build());

        // インベントリ設定
        this.addRenderableWidget(
            Button.builder(
                Component.translatable("hcutilsmod.settings.inventory.title").append("..."),
                (var1) -> this.getMinecraft().setScreen(new InventorySettingsScreen(this)))
            .pos(getPosX() + (getWidth() - 180) / 2, getPosY() + 55)
            .size(180, 20)
            .build());

        // スポナーの検索設定
        this.addRenderableWidget(
            Button.builder(
                Component.translatable("hcutilsmod.settings.findspawner.title").append("..."),
                (var1) -> this.getMinecraft().setScreen(new FindSpawnerSettingsScreen(this)))
            .pos(getPosX() + (getWidth() - 180) / 2, getPosY() + 80)
            .size(180, 20)
            .build());

        // 明るさ表示設定
//        this.addRenderableWidget(
//            Button.builder(
//                Component.translatable("hcutilsmod.settings.brightness.title").append("..."),
//                (var1) -> this.getMinecraft().setScreen(new BrightnessSettingsScreen(this)))
//            .pos(getPosX() + (getWidth() - 180) / 2, getPosY() + 105)
//            .size(180, 20)
//            .build());

        // ゲームに戻る
        this.addRenderableWidget(
            Button.builder(
                Component.translatable("hcutilsmod.settings.return"),
                (var1) -> this.getMinecraft().setScreen(null))
            .pos(getPosX() + (getWidth() - 100) / 2, getPosY() + 165)
            .size(100, 20)
            .build());
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
