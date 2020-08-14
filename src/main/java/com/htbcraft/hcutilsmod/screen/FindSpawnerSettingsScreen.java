package com.htbcraft.hcutilsmod.screen;

import com.htbcraft.hcutilsmod.common.HCSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.SliderPercentageOption;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class FindSpawnerSettingsScreen extends SettingsScreen {
    public FindSpawnerSettingsScreen(Screen parent) {
        super(parent, new TranslationTextComponent("hcutilsmod.settings.findspawner.title"));
    }

    public void func_231158_b_(Minecraft p_231158_1_, int p_231158_2_, int p_231158_3_) {
        super.func_231158_b_(p_231158_1_, p_231158_2_, p_231158_3_);

        // オンオフ
        this.func_230480_a_(new Button(getPosX() + (getWidth() - 180) / 2, getPosY() + 30, 180, 20,
                getEnableFindSpawnerModText(), (var1) -> {
            HCSettings.getInstance().enableFindSpawnerMod = !HCSettings.getInstance().enableFindSpawnerMod;
            var1.func_238482_a_(getEnableFindSpawnerModText());
        }));

        // 検索の範囲
        this.func_230480_a_(RENGE.createWidget(p_231158_1_.gameSettings, getPosX() + (getWidth() - 180) / 2, getPosY() + 55, 180));

        // 座標の表示時間
        this.func_230480_a_(TIME.createWidget(p_231158_1_.gameSettings, getPosX() + (getWidth() - 180) / 2, getPosY() + 80, 180));

        // 戻る
        this.func_230480_a_(new Button(getPosX() + (getWidth() - 100) / 2, getPosY() + 140, 100, 20,
                ITextComponent.func_244388_a(I18n.format("hcutilsmod.settings.findspawner.return")),
                (var1) -> this.getMinecraft().displayGuiScreen(this.getParent())));
    }

    private ITextComponent getEnableFindSpawnerModText() {
        if (HCSettings.getInstance().enableFindSpawnerMod) {
            return ITextComponent.func_244388_a(I18n.format("hcutilsmod.settings.findspawner.enable"));
        }
        else {
            return ITextComponent.func_244388_a(I18n.format("hcutilsmod.settings.findspawner.disable"));
        }
    }

    public boolean func_231178_ax__() {
        return false;
    }   // この画面はESCをはじく

    // 検索の範囲
    public static final SliderPercentageOption RENGE = new SliderPercentageOption("hcutilsmod.settings.findspawner.range",
            16.0D, 64.0D, 16.0F,
            (gameSettings) -> (double)HCSettings.getInstance().rangeFindSpawner,
            (gameSettings, value) -> HCSettings.getInstance().rangeFindSpawner = value.intValue(),
            (gameSettings, translationKey) -> {
                IFormattableTextComponent s = (new TranslationTextComponent("hcutilsmod.settings.findspawner.range")).func_240702_b_(": ");
                return s.func_230529_a_(new TranslationTextComponent("" + HCSettings.getInstance().rangeFindSpawner));
            });

    // 座標の表示時間
    public static final SliderPercentageOption TIME = new SliderPercentageOption("hcutilsmod.settings.findspawner.time",
            10.0D, 60.0D, 10.0F,
            (gameSettings) -> (double)HCSettings.getInstance().timeFindSpawner,
            (gameSettings, value) -> HCSettings.getInstance().timeFindSpawner = value.longValue(),
            (gameSettings, translationKey) -> {
                IFormattableTextComponent s = (new TranslationTextComponent("hcutilsmod.settings.findspawner.time")).func_240702_b_(": ");
                return s.func_230529_a_(new TranslationTextComponent(HCSettings.getInstance().timeFindSpawner + I18n.format("hcutilsmod.settings.findspawner.seconds")));
            });
}
