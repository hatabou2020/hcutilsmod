package com.htbcraft.hcutilsmod.screen;

import com.htbcraft.hcutilsmod.common.HCCrypt;
import com.htbcraft.hcutilsmod.common.HCSettings;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class MainSettingsScreen extends SettingsScreen {
    public MainSettingsScreen() {
        super(null, new TranslationTextComponent("hcutilsmod.settings.title"));
    }

    protected void init() {
        super.init();

        // プレイヤー座標の表示・非表示
        this.addButton(new Button(getPosX() + (getWidth() - 180) / 2, getPosY() + 30, 180, 20,
            getEnableCordsModText(),
            (var1) -> {
                HCSettings.getInstance().enableCordsMod = !HCSettings.getInstance().enableCordsMod;
                var1.setMessage(getEnableCordsModText());
            }));

        // インベントリのソート種類
        this.addButton(new Button(getPosX() + (getWidth() - 180) / 2, getPosY() + 55, 180, 20,
            getInventorySortTypeText(),
            (var1) -> {
                HCSettings.SortType[] values = HCSettings.SortType.values();
                int sortType = HCSettings.getInstance().sortType.ordinal();
                if (++sortType >= values.length) {
                    sortType = 0;
                }
                HCSettings.getInstance().sortType = values[sortType];
                var1.setMessage(getInventorySortTypeText());
            }));

        // スポナーの検索設定
        this.addButton(new Button(getPosX() + (getWidth() - 180) / 2, getPosY() + 80, 180, 20,
            new TranslationTextComponent("hcutilsmod.settings.findspawner.title").append("..."),
            (var1) -> this.getMinecraft().setScreen(new FindSpawnerSettingsScreen(this))));

        // 明るさ表示設定
        this.addButton(new Button(getPosX() + (getWidth() - 180) / 2, getPosY() + 105, 180, 20,
            new TranslationTextComponent("hcutilsmod.settings.brightness.title").append("..."),
            (var1) -> this.getMinecraft().setScreen(new BrightnessSettingsScreen(this))));

        if (HCCrypt.isSupportOS()) {
            // Twitter設定
            this.addButton(new Button(getPosX() + (getWidth() - 180) / 2, getPosY() + 130, 180, 20,
                new TranslationTextComponent("hcutilsmod.settings.twitter.title").append("..."),
                (var1) -> this.getMinecraft().setScreen(new TwitterSettingsScreen(this))));
        }

        // ゲームに戻る
        this.addButton(new Button(getPosX() + (getWidth() - 100) / 2, getPosY() + 165, 100, 20,
            new TranslationTextComponent("hcutilsmod.settings.return"),
            (var1) -> this.getMinecraft().setScreen(null)));
    }

    private ITextComponent getEnableCordsModText() {
        if (HCSettings.getInstance().enableCordsMod) {
            return new TranslationTextComponent("hcutilsmod.settings.cords.enable");
        }
        else {
            return new TranslationTextComponent("hcutilsmod.settings.cords.disable");
        }
    }

    private ITextComponent getInventorySortTypeText() {
        if (HCSettings.getInstance().sortType == HCSettings.SortType.CATEGORY) {
            return new TranslationTextComponent("hcutilsmod.settings.inventory.sorttype.category");
        }
        else {
            return new TranslationTextComponent("hcutilsmod.settings.inventory.sorttype.name");
        }
    }
}
