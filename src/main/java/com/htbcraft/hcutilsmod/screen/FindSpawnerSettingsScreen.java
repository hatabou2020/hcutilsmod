package com.htbcraft.hcutilsmod.screen;

import com.htbcraft.hcutilsmod.common.HCSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.SliderPercentageOption;
import net.minecraft.util.text.TranslationTextComponent;

public class FindSpawnerSettingsScreen extends SettingsScreen {
    public FindSpawnerSettingsScreen(Screen parent) {
        super(parent, new TranslationTextComponent("hcutilsmod.settings.findspawner.title"));
    }

    public void init(Minecraft p_init_1_, int p_init_2_, int p_init_3_) {
        super.init(p_init_1_, p_init_2_, p_init_3_);

        // オンオフ
        this.addButton(new Button(getPosX() + (getWidth() - 180) / 2, getPosY() + 30, 180, 20,
                getEnableFindSpawnerModText(), (var1) -> {
            HCSettings.getInstance().enableFindSpawnerMod = !HCSettings.getInstance().enableFindSpawnerMod;
            var1.setMessage(getEnableFindSpawnerModText());
        }));

        // 検索の範囲
        this.addButton(RENGE.createWidget(p_init_1_.gameSettings, getPosX() + (getWidth() - 180) / 2, getPosY() + 55, 180));

        // 座標の表示時間
        this.addButton(TIME.createWidget(p_init_1_.gameSettings, getPosX() + (getWidth() - 180) / 2, getPosY() + 80, 180));

        // 戻る
        this.addButton(new Button(getPosX() + (getWidth() - 100) / 2, getPosY() + 140, 100, 20,
                I18n.format("hcutilsmod.settings.findspawner.return"),
                (var1) -> this.getMinecraft().displayGuiScreen(this.getParent())));
    }

    private String getEnableFindSpawnerModText() {
        if (HCSettings.getInstance().enableFindSpawnerMod) {
            return I18n.format("hcutilsmod.settings.findspawner.enable");
        }
        else {
            return I18n.format("hcutilsmod.settings.findspawner.disable");
        }
    }

    public boolean shouldCloseOnEsc() {
        return false;
    }   // この画面はESCをはじく

    // 検索の範囲
    public static final SliderPercentageOption RENGE = new SliderPercentageOption("hcutilsmod.settings.findspawner.range",
            16.0D, 64.0D, 16.0F,
            (gameSettings) -> (double)HCSettings.getInstance().rangeFindSpawner,
            (gameSettings, value) -> HCSettings.getInstance().rangeFindSpawner = value.intValue(),
            (gameSettings, translationKey) -> {
                return translationKey.getDisplayString() + HCSettings.getInstance().rangeFindSpawner;
            });

    // 座標の表示時間
    public static final SliderPercentageOption TIME = new SliderPercentageOption("hcutilsmod.settings.findspawner.time",
            10.0D, 60.0D, 10.0F,
            (gameSettings) -> (double)HCSettings.getInstance().timeFindSpawner,
            (gameSettings, value) -> HCSettings.getInstance().timeFindSpawner = value.longValue(),
            (gameSettings, translationKey) -> {
                return translationKey.getDisplayString() + HCSettings.getInstance().timeFindSpawner + I18n.format("hcutilsmod.settings.findspawner.seconds");
            });
}
