package com.htbcraft.hcutilsmod.screen;

import com.htbcraft.hcutilsmod.common.HCSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.GameSettings;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.SliderPercentageOption;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class BrightnessSettingsScreen extends SettingsScreen {
    public BrightnessSettingsScreen(Screen parent) {
        super(parent, new TranslationTextComponent("hcutilsmod.settings.brightness.title"));
    }

    protected void init() {
        super.init();

        GameSettings options = Minecraft.getInstance().options;

        // 表示の範囲
        this.addButton(RENGE.createButton(options, getPosX() + (getWidth() - 180) / 2, getPosY() + 30, 180));

        // 明るさの閾値
        this.addButton(THRESHOLD.createButton(options, getPosX() + (getWidth() - 180) / 2, getPosY() + 55, 180));

        // ゾンビが湧くことができないブロックを除外するか
        this.addButton(new Button(getPosX() + (getWidth() - 180) / 2, getPosY() + 80, 180, 20,
            getZombieBrightnessText(),
            (var1) -> {
                HCSettings.getInstance().zombieBrightness = !HCSettings.getInstance().zombieBrightness;
                var1.setMessage(getZombieBrightnessText());
            }));

        // マーカーの詳細設定
        this.addButton(new Button(getPosX() + (getWidth() - 180) / 2, getPosY() + 105, 180, 20,
            ITextComponent.nullToEmpty(I18n.get("hcutilsmod.settings.brightness.subtitle") + "..."),
            (var1) -> this.getMinecraft().setScreen(new BrightnessDetailSettingsScreen(this))));

        // 戻る
        this.addButton(new Button(getPosX() + (getWidth() - 100) / 2, getPosY() + 140, 100, 20,
            ITextComponent.nullToEmpty(I18n.get("hcutilsmod.settings.brightness.return")),
            (var1) -> this.getMinecraft().setScreen(this.getParent())));
    }

    public boolean shouldCloseOnEsc() {
        return false;
    }   // この画面はESCをはじく

    // 表示の範囲
    public static final SliderPercentageOption RENGE = new SliderPercentageOption("hcutilsmod.settings.brightness.range",
            1.0D, 16.0D, 1.0F,
            (gameSettings) -> (double)HCSettings.getInstance().rangeBrightness,
            (gameSettings, value) -> HCSettings.getInstance().rangeBrightness = value.intValue(),
            (gameSettings, translationKey) ->
                (new TranslationTextComponent("hcutilsmod.settings.brightness.range"))
                    .append(": " + HCSettings.getInstance().rangeBrightness + " blocks"));

    // 明るさの閾値
    public static final SliderPercentageOption THRESHOLD = new SliderPercentageOption("hcutilsmod.settings.brightness.threshold",
            0.0D, 14.0D, 1.0F,
            (gameSettings) -> (double)HCSettings.getInstance().thresholdBrightness,
            (gameSettings, value) -> HCSettings.getInstance().thresholdBrightness = value.intValue(),
            (gameSettings, translationKey) ->
                (new TranslationTextComponent("hcutilsmod.settings.brightness.threshold"))
                    .append(": brightness ≦ " + HCSettings.getInstance().thresholdBrightness));

    private ITextComponent getZombieBrightnessText() {
        if (HCSettings.getInstance().zombieBrightness) {
            return ITextComponent.nullToEmpty(I18n.get("hcutilsmod.settings.brightness.zombie.without"));
        }
        else {
            return ITextComponent.nullToEmpty(I18n.get("hcutilsmod.settings.brightness.zombie.with"));
        }
    }
}
