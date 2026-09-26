package com.htbcraft.hcutilsmod.mods.brightness;

import com.htbcraft.hcutilsmod.common.MinecraftColor;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ARGB;

public class BrightnessMarker {
    private final int argb;
    private final BlockPos pos;

    public BrightnessMarker(MinecraftColor color, int alpha, BlockPos pos) {
        this.argb = ARGB.color(alpha, color.getRed(), color.getGreen(), color.getBlue());
        this.pos = pos;
    }

    public void draw(PoseStack poseStack) {
        // 描画処理を実装する
    }
}
