package com.htbcraft.hcutilsmod.screen;

import com.htbcraft.hcutilsmod.common.HCSettings;
import net.minecraft.client.ProgressOption;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;

public class FindSpawnerSettingsScreen extends SettingsScreen {
    public FindSpawnerSettingsScreen(Screen parent) {
        super(parent, new TranslatableComponent("hcutilsmod.settings.findspawner.title"));
    }

    protected void init() {
        super.init();

        // オンオフ
        this.addRenderableWidget(new Button(getPosX() + (getWidth() - 180) / 2, getPosY() + 30, 180, 20,
                getEnableFindSpawnerModText(), (var1) -> {
            HCSettings.getInstance().enableFindSpawnerMod = !HCSettings.getInstance().enableFindSpawnerMod;
            var1.setMessage(getEnableFindSpawnerModText());
        }));

        // 検索の範囲
        this.addRenderableWidget(RENGE.createButton(minecraft.options, getPosX() + (getWidth() - 180) / 2, getPosY() + 55, 180));

        // 座標の表示時間
        this.addRenderableWidget(TIME.createButton(minecraft.options, getPosX() + (getWidth() - 180) / 2, getPosY() + 80, 180));

        // 戻る
        this.addRenderableWidget(new Button(getPosX() + (getWidth() - 100) / 2, getPosY() + 140, 100, 20,
                        new TranslatableComponent("hcutilsmod.settings.findspawner.return"),
                        (var1) -> this.getMinecraft().setScreen(this.getParent())));
    }

    private TranslatableComponent getEnableFindSpawnerModText() {
        if (HCSettings.getInstance().enableFindSpawnerMod) {
            return new TranslatableComponent("hcutilsmod.settings.findspawner.enable");
        }
        else {
            return new TranslatableComponent("hcutilsmod.settings.findspawner.disable");
        }
    }

    public boolean shouldCloseOnEsc() {
        return false;
    }   // この画面はESCをはじく

    // 検索の範囲
    public static final ProgressOption RENGE = new ProgressOption("hcutilsmod.settings.findspawner.range",
            16.0D, 64.0D, 16.0F,
            (gameSettings) -> (double)HCSettings.getInstance().rangeFindSpawner,
            (gameSettings, value) -> HCSettings.getInstance().rangeFindSpawner = value.intValue(),
            (gameSettings, translationKey) -> {
                return (new TranslatableComponent("hcutilsmod.settings.findspawner.range"))
                        .append(": " + HCSettings.getInstance().rangeFindSpawner);
            });

    // 座標の表示時間
    public static final ProgressOption TIME = new ProgressOption("hcutilsmod.settings.findspawner.time",
            10.0D, 60.0D, 10.0F,
            (gameSettings) -> (double)HCSettings.getInstance().timeFindSpawner,
            (gameSettings, value) -> HCSettings.getInstance().timeFindSpawner = value.longValue(),
            (gameSettings, translationKey) -> {
                return (new TranslatableComponent("hcutilsmod.settings.findspawner.time"))
                        .append(": " + HCSettings.getInstance().timeFindSpawner)
                        .append(new TranslatableComponent("hcutilsmod.settings.findspawner.seconds"));
            });
}
