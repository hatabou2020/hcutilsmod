package com.htbcraft.hcutilsmod.screen;

import com.htbcraft.hcutilsmod.common.HCSettings;
import com.htbcraft.hcutilsmod.common.MinecraftColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class BrightnessDetailSettingsScreen extends SettingsScreen {
    public BrightnessDetailSettingsScreen(Screen parent) {
        super(parent, Component.translatable("hcutilsmod.settings.brightness.subtitle"));
    }

    protected void init() {
        super.init();

        Options options = Minecraft.getInstance().options;

        // マーカーの色
        this.addRenderableWidget(new Button(getPosX() + (getWidth() - 180) / 2, getPosY() + 30, 180, 20,
                getMarkerColorText(), (var1) -> {
            MinecraftColor[] values = MinecraftColor.values();
            int color = HCSettings.getInstance().colorBrightness.ordinal();
            if (++color >= values.length) {
                color = 0;
            }
            HCSettings.getInstance().colorBrightness = values[color];
            var1.setMessage(getMarkerColorText());
        }));

//        // マーカーの透過度
//        this.addRenderableWidget(ALPHA.createButton(options, getPosX() + (getWidth() - 180) / 2, getPosY() + 55, 180));

        // 戻る
        this.addRenderableWidget(new Button(getPosX() + (getWidth() - 100) / 2, getPosY() + 140, 100, 20,
                Component.translatable("hcutilsmod.settings.brightness.return"),
                (var1) -> this.getMinecraft().setScreen(this.getParent())));
    }

    public boolean shouldCloseOnEsc() {
        return false;
    }   // この画面はESCをはじく

    // マーカーの透過度
//    public static final ProgressOption ALPHA = new ProgressOption("hcutilsmod.settings.brightness.alpha",
//            1.0D, 255.0D, 1.0F,
//            (gameSettings) -> (double)HCSettings.getInstance().alphaBrightness,
//            (gameSettings, value) -> HCSettings.getInstance().alphaBrightness = value.intValue(),
//            (gameSettings, translationKey) ->
//	            (Component.translatable("hcutilsmod.settings.brightness.alpha"))
//	                .append(": " + HCSettings.getInstance().alphaBrightness));

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
