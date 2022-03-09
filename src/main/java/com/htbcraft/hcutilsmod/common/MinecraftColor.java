package com.htbcraft.hcutilsmod.common;

// https://www.colordic.org/
public enum MinecraftColor {
    WHITE(0xFFFFFF),        // 白色 (=white)
    BLACK(0x000000),        // 黒色 (=black)
    GRAY(0x808080),         // 灰色 (=gray)
    LIGHT_GRAY(0xC0C0C0),   // 薄灰色 (=silver)
    BROWN(0x8B4513),        // 茶色 (=saddlebrown)
    RED(0xFF0000),          // 赤色 (=red)
    ORANGE(0xFFA500),       // 橙色 (=orange)
    YELLOW(0xFFFF00),       // 黄色 (=yellow)
    GREEN(0x008000),        // 緑色 (=green)
    LIME(0x00FF00),         // 黄緑色 (=lime)
    BLUE(0x0000FF),         // 青色 (=blue)
    CYAN(0x008B8B),         // 青緑色 (=darkcyan)
    LIGHT_BLUE(0x00FFFF),   // 空色 (=cyan)
    PURPLE(0x800080),       // 紫色 (=purple)
    MAGENTA(0xFF00FF),      // 赤紫色 (=magenta)
    PINK(0xFFC0CB);         // 桃色 (=pink)

    private final int rgb;

    MinecraftColor(int rgb) {
        this.rgb = rgb;
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

    @Override
    public String toString() {
        return "MinecraftColor{" +
                "red=" + Integer.toHexString(getRed()) +
                ", green=" + Integer.toHexString(getGreen()) +
                ", blue=" + Integer.toHexString(getBlue()) +
                '}';
    }
}
