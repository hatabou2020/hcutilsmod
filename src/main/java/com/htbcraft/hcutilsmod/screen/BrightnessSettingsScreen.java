package com.htbcraft.hcutilsmod.screen;

import com.htbcraft.hcutilsmod.common.HCSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class BrightnessSettingsScreen extends SettingsScreen {
    public BrightnessSettingsScreen(Screen parent) {
        super(parent, Component.translatable("hcutilsmod.settings.brightness.title"));
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
            getZombieBrightnessText(),
            (var1) -> {
	            HCSettings.getInstance().zombieBrightness = !HCSettings.getInstance().zombieBrightness;
	            var1.setMessage(getZombieBrightnessText());
	        }));

        // マーカーの詳細設定
        this.addRenderableWidget(new Button(getPosX() + (getWidth() - 180) / 2, getPosY() + 105, 180, 20,
            Component.translatable("hcutilsmod.settings.brightness.subtitle").append("..."),
            (var1) -> this.getMinecraft().setScreen(new BrightnessDetailSettingsScreen(this))));

        // 戻る
        this.addRenderableWidget(new Button(getPosX() + (getWidth() - 100) / 2, getPosY() + 140, 100, 20,
            Component.translatable("hcutilsmod.settings.brightness.return"),
            (var1) -> this.getMinecraft().setScreen(this.getParent())));
    }

    public boolean shouldCloseOnEsc() {
        return false;
    }   // この画面はESCをはじく

    // 表示の範囲
    private static final OptionInstance<Integer> RENGE = new OptionInstance<>(
        "hcutilsmod.settings.brightness.range",
        OptionInstance.noTooltip(),
        (label, value) -> {
            return Component.translatable("hcutilsmod.settings.brightness.range")
                    .append(": " + HCSettings.getInstance().rangeBrightness + " blocks");
        },
        new OptionInstance.IntRange(1, 16),
        HCSettings.getInstance().rangeBrightness,
        (value) -> {
            HCSettings.getInstance().rangeBrightness = value;
        }
    );

    // 明るさの閾値
    private static final OptionInstance<Integer> THRESHOLD = new OptionInstance<>(
        "hcutilsmod.settings.brightness.threshold",
        OptionInstance.noTooltip(),
        (label, value) -> {
            return Component.translatable("hcutilsmod.settings.brightness.threshold")
                    .append(": brightness ≦ " + HCSettings.getInstance().thresholdBrightness);
        },
        new OptionInstance.IntRange(0, 14),
        HCSettings.getInstance().thresholdBrightness,
        (value) -> {
            HCSettings.getInstance().thresholdBrightness = value;
        }
    );

    private Component getZombieBrightnessText() {
        if (HCSettings.getInstance().zombieBrightness) {
            return Component.translatable("hcutilsmod.settings.brightness.zombie.without");
        }
        else {
            return Component.translatable("hcutilsmod.settings.brightness.zombie.with");
        }
    }
}
