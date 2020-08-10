package com.htbcraft.hcutilsmod.screen;

import com.htbcraft.hcutilsmod.common.HCSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class MainSettingsScreen extends SettingsScreen {
    public MainSettingsScreen() {
        super(new TranslationTextComponent("hcutilsmod.settings.title"));
    }

    public void func_231158_b_(Minecraft p_231158_1_, int p_231158_2_, int p_231158_3_) {
        super.func_231158_b_(p_231158_1_, p_231158_2_, p_231158_3_);

        // プレイヤー座標の表示設定
        this.func_230480_a_(new Button(getPosX() + (getWidth() - 180) / 2, getPosY() + 30, 180, 20, getEnableCordsModText(), (var1) -> {
            HCSettings.getInstance().enableCordsMod = !HCSettings.getInstance().enableCordsMod;
            var1.func_238482_a_(getEnableCordsModText());
        }));
    }

    private ITextComponent getEnableCordsModText() {
        if (HCSettings.getInstance().enableCordsMod) {
            return ITextComponent.func_241827_a_(I18n.format("hcutilsmod.settings.cords.enable"));
        }
        else {
            return ITextComponent.func_241827_a_(I18n.format("hcutilsmod.settings.cords.disable"));
        }
    }
}
