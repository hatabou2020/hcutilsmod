package com.htbcraft.hcutilsmod.common;

import net.minecraft.network.chat.Component;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// https://www.colordic.org/
public enum MinecraftColor {
    WHITE(0xFFFFFF, "white"),           // 白色 (=white)
    BLACK(0x000000, "black"),           // 黒色 (=black)
    GRAY(0x808080, "gray"),             // 灰色 (=gray)
    LIGHT_GRAY(0xC0C0C0, "light_gray"), // 薄灰色 (=silver)
    BROWN(0x8B4513, "brown"),           // 茶色 (=saddlebrown)
    RED(0xFF0000, "red"),               // 赤色 (=red)
    ORANGE(0xFFA500, "orange"),         // 橙色 (=orange)
    YELLOW(0xFFFF00, "yellow"),         // 黄色 (=yellow)
    GREEN(0x008000, "green"),           // 緑色 (=green)
    LIME(0x00FF00, "lime"),             // 黄緑色 (=lime)
    BLUE(0x0000FF, "blue"),             // 青色 (=blue)
    CYAN(0x008B8B, "cyan"),             // 青緑色 (=darkcyan)
    LIGHT_BLUE(0x00FFFF, "light_blue"), // 空色 (=cyan)
    PURPLE(0x800080, "purple"),         // 紫色 (=purple)
    MAGENTA(0xFF00FF, "magenta"),       // 赤紫色 (=magenta)
    PINK(0xFFC0CB, "pink");             // 桃色 (=pink)

    private final int rgb;
    private final Component label;

    MinecraftColor(int rgb, String key) {
        this.rgb = rgb;
        this.label = Component.translatable("common.minecraft.color." + key);
    }

    public int getRGB() {
        return this.rgb;
    }

    public int getRed() {
        return (this.rgb >> 16) & 0x000000FF;
    }

    public int getGreen() {
        return (this.rgb >> 8) & 0x000000FF;
    }

    public int getBlue() {
        return this.rgb & 0x000000FF;
    }

    public static List<Component> getLabels() {
        ArrayList<Component> labels = new ArrayList<>();
        for (MinecraftColor e : values()) {
            labels.add(e.label);
        }
        return labels;
    }

    @Override
    public String toString() {
        return this.label.getString();
    }
}
