package com.htbcraft.hcutilsmod.screen;

import com.htbcraft.hcutilsmod.common.HCSettings;
import com.htbcraft.hcutilsmod.common.MinecraftColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.client.Options;

public class BrightnessDetailSettingsScreen extends SettingsScreen {
    public BrightnessDetailSettingsScreen(Screen parent) {
        super(parent, Component.translatable("hcutilsmod.settings.brightness.subtitle"));
    }

    protected void init() {
        super.init();

        Options options = Minecraft.getInstance().options;

        // マーカーの色
        this.addRenderableWidget(
                Button.builder(
                    getMarkerColorText(),
                    (var1) -> {
                        MinecraftColor[] values = MinecraftColor.values();
                        int color = HCSettings.getInstance().colorBrightness.ordinal();
                        if (++color >= values.length) {
                            color = 0;
                        }
                        HCSettings.getInstance().colorBrightness = values[color];
                        var1.setMessage(getMarkerColorText());
                    })
                .pos(getPosX() + (getWidth() - 180) / 2, getPosY() + 30)
                .size(180, 20)
                .build());

        // マーカーの透過度
        this.addRenderableWidget(ALPHA.createButton(options, getPosX() + (getWidth() - 180) / 2, getPosY() + 55, 180));

        // 戻る
        this.addRenderableWidget(
                Button.builder(
                    Component.translatable("hcutilsmod.settings.brightness.return"),
                    (var1) -> this.getMinecraft().setScreen(this.getParent()))
                .pos(getPosX() + (getWidth() - 100) / 2, getPosY() + 140)
                .size(100, 20)
                .build());
    }

    public boolean shouldCloseOnEsc() {
        return false;
    }   // この画面はESCをはじく

    // マーカーの透過度
    private static final OptionInstance<Integer> ALPHA = new OptionInstance<>(
        "hcutilsmod.settings.brightness.alpha",
        OptionInstance.noTooltip(),
        (label, value) -> {
            return Component.translatable("hcutilsmod.settings.brightness.alpha")
                    .append(": " + HCSettings.getInstance().alphaBrightness);
        },
        new OptionInstance.IntRange(1, 255),
        HCSettings.getInstance().alphaBrightness,
        (value) -> {
            HCSettings.getInstance().alphaBrightness = value;
        }
    );

    private Component getMarkerColorText() {
        if (HCSettings.getInstance().colorBrightness == MinecraftColor.WHITE) {
            return Component.translatable("hcutilsmod.settings.brightness.color.white");
        }
        else if (HCSettings.getInstance().colorBrightness == MinecraftColor.BLACK) {
            return Component.translatable("hcutilsmod.settings.brightness.color.black");
        }
        else if (HCSettings.getInstance().colorBrightness == MinecraftColor.GRAY) {
            return Component.translatable("hcutilsmod.settings.brightness.color.gray");
        }
        else if (HCSettings.getInstance().colorBrightness == MinecraftColor.LIGHT_GRAY) {
            return Component.translatable("hcutilsmod.settings.brightness.color.light_gray");
        }
        else if (HCSettings.getInstance().colorBrightness == MinecraftColor.BROWN) {
            return Component.translatable("hcutilsmod.settings.brightness.color.brown");
        }
        else if (HCSettings.getInstance().colorBrightness == MinecraftColor.ORANGE) {
            return Component.translatable("hcutilsmod.settings.brightness.color.orange");
        }
        else if (HCSettings.getInstance().colorBrightness == MinecraftColor.YELLOW) {
            return Component.translatable("hcutilsmod.settings.brightness.color.yellow");
        }
        else if (HCSettings.getInstance().colorBrightness == MinecraftColor.GREEN) {
            return Component.translatable("hcutilsmod.settings.brightness.color.green");
        }
        else if (HCSettings.getInstance().colorBrightness == MinecraftColor.LIME) {
            return Component.translatable("hcutilsmod.settings.brightness.color.lime");
        }
        else if (HCSettings.getInstance().colorBrightness == MinecraftColor.BLUE) {
            return Component.translatable("hcutilsmod.settings.brightness.color.blue");
        }
        else if (HCSettings.getInstance().colorBrightness == MinecraftColor.CYAN) {
            return Component.translatable("hcutilsmod.settings.brightness.color.cyan");
        }
        else if (HCSettings.getInstance().colorBrightness == MinecraftColor.LIGHT_BLUE) {
            return Component.translatable("hcutilsmod.settings.brightness.color.light_blue");
        }
        else if (HCSettings.getInstance().colorBrightness == MinecraftColor.PURPLE) {
            return Component.translatable("hcutilsmod.settings.brightness.color.purple");
        }
        else if (HCSettings.getInstance().colorBrightness == MinecraftColor.MAGENTA) {
            return Component.translatable("hcutilsmod.settings.brightness.color.magenta");
        }
        else if (HCSettings.getInstance().colorBrightness == MinecraftColor.PINK) {
            return Component.translatable("hcutilsmod.settings.brightness.color.pink");
        }
        else {
            return Component.translatable("hcutilsmod.settings.brightness.color.red");
        }
    }
}
