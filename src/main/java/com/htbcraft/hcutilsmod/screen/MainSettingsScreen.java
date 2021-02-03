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

    public void init(Minecraft minecraft, int width, int height) {
        super.init(minecraft, width, height);

        // プレイヤー座標の表示・非表示
        this.addButton(new Button(getPosX() + (getWidth() - 180) / 2, getPosY() + 30, 180, 20,
                getEnableCordsModText(), (var1) -> {
            HCSettings.getInstance().enableCordsMod = !HCSettings.getInstance().enableCordsMod;
            var1.setMessage(getEnableCordsModText());
        }));

        // インベントリのソート種類
        this.addButton(new Button(getPosX() + (getWidth() - 180) / 2, getPosY() + 55, 180, 20,
                getInventorySortTypeText(), (var1) -> {
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
                ITextComponent.getTextComponentOrEmpty(I18n.format("hcutilsmod.settings.findspawner.title") + "…"),
                (var1) -> this.getMinecraft().displayGuiScreen(new FindSpawnerSettingsScreen(this))));

        // ゲームに戻る
        this.addButton(new Button(getPosX() + (getWidth() - 100) / 2, getPosY() + 140, 100, 20,
                ITextComponent.getTextComponentOrEmpty(I18n.format("hcutilsmod.settings.return")),
                (var1) -> this.getMinecraft().displayGuiScreen(null)));
    }

    private ITextComponent getEnableCordsModText() {
        if (HCSettings.getInstance().enableCordsMod) {
            return ITextComponent.getTextComponentOrEmpty(I18n.format("hcutilsmod.settings.cords.enable"));
        }
        else {
            return ITextComponent.getTextComponentOrEmpty(I18n.format("hcutilsmod.settings.cords.disable"));
        }
    }

    private ITextComponent getInventorySortTypeText() {
        if (HCSettings.getInstance().sortType == NAME) {
            return ITextComponent.getTextComponentOrEmpty(I18n.format("hcutilsmod.settings.inventory.sorttype.name"));
        }
        else {
            return ITextComponent.getTextComponentOrEmpty(I18n.format("hcutilsmod.settings.inventory.sorttype.name"));
        }
    }
}
