package com.htbcraft.hcutilsmod.screen;

import com.htbcraft.hcutilsmod.common.HCSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class FindSpawnerSettingsScreen extends SettingsScreen {
    public FindSpawnerSettingsScreen(Screen parent) {
        super(parent, Component.translatable("hcutilsmod.settings.findspawner.title"));
    }

    protected void init() {
        super.init();

        // オンオフ
        this.addRenderableWidget(
                Button.builder(
                    getEnableFindSpawnerModText(),
                    (var1) -> {
                        HCSettings.getInstance().enableFindSpawnerMod = !HCSettings.getInstance().enableFindSpawnerMod;
                        var1.setMessage(getEnableFindSpawnerModText());
                    })
                .pos(getPosX() + (getWidth() - 180) / 2, getPosY() + 30)
                .size(180, 20)
                .build());

        Options options = Minecraft.getInstance().options;

        // 検索の範囲
        this.addRenderableWidget(RENGE.createButton(options, getPosX() + (getWidth() - 180) / 2, getPosY() + 55, 180));

        // 座標の表示時間
        this.addRenderableWidget(TIME.createButton(options, getPosX() + (getWidth() - 180) / 2, getPosY() + 80, 180));

        // 戻る
        this.addRenderableWidget(
                Button.builder(
                    Component.translatable("hcutilsmod.settings.findspawner.return"),
                    (var1) -> this.getMinecraft().setScreen(this.getParent()))
                .pos(getPosX() + (getWidth() - 100) / 2, getPosY() + 140)
                .size(100, 20)
                .build());
    }

    private Component getEnableFindSpawnerModText() {
        if (HCSettings.getInstance().enableFindSpawnerMod) {
            return Component.translatable("hcutilsmod.settings.findspawner.enable");
        }
        else {
            return Component.translatable("hcutilsmod.settings.findspawner.disable");
        }
    }

    public boolean shouldCloseOnEsc() {
        return false;
    }   // この画面はESCをはじく

    // 検索の範囲
    private static final OptionInstance<Integer> RENGE = new OptionInstance<>(
        "hcutilsmod.settings.findspawner.range",
        OptionInstance.noTooltip(),
        (label, value) -> {
            return Component.translatable("hcutilsmod.settings.findspawner.range")
                    .append(": " + HCSettings.getInstance().rangeFindSpawner + " blocks");
        },
        (new OptionInstance.IntRange(1, 4)).xmap(
                (value) -> {
                    return value * 16;
                },
                (value) -> {
                    return value / 16;
                }
        ),
        HCSettings.getInstance().rangeFindSpawner,
        (value) -> {
            HCSettings.getInstance().rangeFindSpawner = value;
        }
    );

    // 座標の表示時間
    private static final OptionInstance<Integer> TIME = new OptionInstance<>(
        "hcutilsmod.settings.findspawner.time",
        OptionInstance.noTooltip(),
        (label, value) -> {
            return Component.translatable("hcutilsmod.settings.findspawner.time")
                    .append(": " + HCSettings.getInstance().timeFindSpawner + " ")
                    .append(Component.translatable("hcutilsmod.settings.findspawner.seconds"));
        },
        (new OptionInstance.IntRange(1, 6)).xmap(
            (value) -> {
                return value * 10;
            },
            (value) -> {
                return value / 10;
            }
        ),
        (int)HCSettings.getInstance().timeFindSpawner,
        (value) -> {
            HCSettings.getInstance().timeFindSpawner = value;
        }
    );
}
