package com.htbcraft.hcutilsmod.screen;

import com.htbcraft.hcutilsmod.common.HCSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import static com.htbcraft.hcutilsmod.common.HCSettings.SortType.NAME;

public class MainSettingsScreen extends SettingsScreen {
    public MainSettingsScreen() {
        super(null, new TranslationTextComponent("hcutilsmod.settings.title"));
    }

    public void func_231158_b_(Minecraft p_231158_1_, int p_231158_2_, int p_231158_3_) {
        super.func_231158_b_(p_231158_1_, p_231158_2_, p_231158_3_);

        // プレイヤー座標の表示・非表示
        this.func_230480_a_(new Button(getPosX() + (getWidth() - 180) / 2, getPosY() + 30, 180, 20,
                getEnableCordsModText(), (var1) -> {
            HCSettings.getInstance().enableCordsMod = !HCSettings.getInstance().enableCordsMod;
            var1.func_238482_a_(getEnableCordsModText());
        }));

        // インベントリのソート種類
        this.func_230480_a_(new Button(getPosX() + (getWidth() - 180) / 2, getPosY() + 55, 180, 20,
                getInventorySortTypeText(), (var1) -> {
            HCSettings.SortType[] values = HCSettings.SortType.values();
            int sortType = HCSettings.getInstance().sortType.ordinal();
            if (++sortType >= values.length) {
                sortType = 0;
            }
            HCSettings.getInstance().sortType = values[sortType];
            var1.func_238482_a_(getInventorySortTypeText());
        }));

        // スポナーの検索設定
        this.func_230480_a_(new Button(getPosX() + (getWidth() - 180) / 2, getPosY() + 80, 180, 20,
                ITextComponent.func_244388_a(I18n.format("hcutilsmod.settings.findspawner.title") + "…"),
                (var1) -> this.getMinecraft().displayGuiScreen(new FindSpawnerSettingsScreen(this))));

        // ゲームに戻る
        this.func_230480_a_(new Button(getPosX() + (getWidth() - 100) / 2, getPosY() + 140, 100, 20,
                ITextComponent.func_244388_a(I18n.format("hcutilsmod.settings.return")),
                (var1) -> this.getMinecraft().displayGuiScreen(null)));
    }

    private ITextComponent getEnableCordsModText() {
        if (HCSettings.getInstance().enableCordsMod) {
            return ITextComponent.func_244388_a(I18n.format("hcutilsmod.settings.cords.enable"));
        }
        else {
            return ITextComponent.func_244388_a(I18n.format("hcutilsmod.settings.cords.disable"));
        }
    }

    private ITextComponent getInventorySortTypeText() {
        if (HCSettings.getInstance().sortType == NAME) {
            return ITextComponent.func_244388_a(I18n.format("hcutilsmod.settings.inventory.sorttype.name"));
        }
        else {
            return ITextComponent.func_244388_a(I18n.format("hcutilsmod.settings.inventory.sorttype.name"));
        }
    }
}
