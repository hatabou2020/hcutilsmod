package com.htbcraft.hcutilsmod.screen;

import com.htbcraft.hcutilsmod.common.HCSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.ProgressOption;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;

public class BrightnessSettingsScreen extends SettingsScreen {
    public BrightnessSettingsScreen(Screen parent) {
        super(parent, new TranslatableComponent("hcutilsmod.settings.brightness.title"));
    }

    protected void init() {
        super.init();

        Options options = Minecraft.getInstance().options;

        // 表示の範囲
        this.addRenderableWidget(RENGE.createButton(options, getPosX() + (getWidth() - 180) / 2, getPosY() + 30, 180));

        // 明るさの閾値
        this.addRenderableWidget(THRESHOLD.createButton(options, getPosX() + (getWidth() - 180) / 2, getPosY() + 55, 180));

        // ゾンビが湧くことができないブロックを除外するか
        this.addRenderableWidget(new Button(getPosX() + (getWidth() - 180) / 2, getPosY() + 80, 180, 20,
                getZombieBrightnessText(), (var1) -> {
            HCSettings.getInstance().zombieBrightness = !HCSettings.getInstance().zombieBrightness;
            var1.setMessage(getZombieBrightnessText());
        }));

        // マーカーの詳細設定
        this.addRenderableWidget(new Button(getPosX() + (getWidth() - 180) / 2, getPosY() + 105, 180, 20,
                new TranslatableComponent("hcutilsmod.settings.brightness.subtitle").append("..."),
                (var1) -> this.getMinecraft().setScreen(new BrightnessDetailSettingsScreen(this))));

        // 戻る
        this.addRenderableWidget(new Button(getPosX() + (getWidth() - 100) / 2, getPosY() + 140, 100, 20,
                new TranslatableComponent("hcutilsmod.settings.brightness.return"),
                (var1) -> this.getMinecraft().setScreen(this.getParent())));
    }

    public boolean shouldCloseOnEsc() {
        return false;
    }   // この画面はESCをはじく

    // 表示の範囲
    public static final ProgressOption RENGE = new ProgressOption("hcutilsmod.settings.brightness.range",
            1.0D, 16.0D, 1.0F,
            (gameSettings) -> (double)HCSettings.getInstance().rangeBrightness,
            (gameSettings, value) -> HCSettings.getInstance().rangeBrightness = value.intValue(),
            (gameSettings, translationKey) -> (new TranslatableComponent("hcutilsmod.settings.brightness.range"))
                    .append(": " + HCSettings.getInstance().rangeBrightness + " blocks"));

    // 明るさの閾値
    public static final ProgressOption THRESHOLD = new ProgressOption("hcutilsmod.settings.brightness.threshold",
            0.0D, 14.0D, 1.0F,
            (gameSettings) -> (double)HCSettings.getInstance().thresholdBrightness,
            (gameSettings, value) -> HCSettings.getInstance().thresholdBrightness = value.intValue(),
            (gameSettings, translationKey) -> (new TranslatableComponent("hcutilsmod.settings.brightness.threshold"))
                    .append(": brightness ≦ " + HCSettings.getInstance().thresholdBrightness));

    private TranslatableComponent getZombieBrightnessText() {
        if (HCSettings.getInstance().zombieBrightness) {
            return new TranslatableComponent("hcutilsmod.settings.brightness.zombie.without");
        }
        else {
            return new TranslatableComponent("hcutilsmod.settings.brightness.zombie.with");
        }
    }
}
