package com.htbcraft.hcutilsmod.mods.brightness;

import com.htbcraft.hcutilsmod.HCUtilsMod;
import com.htbcraft.hcutilsmod.common.MinecraftColor;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.util.ResourceLocation;

public class BrightnessMarkerModel {
    private static final float MARKER_SIZE = 0.6F;
    private static final float MARKER_OFFSET = 0.01F;

    private static final ResourceLocation MARKER = new ResourceLocation(HCUtilsMod.MOD_ID, "textures/white.png");

    private final float minX;
    private final float maxX;
    private final float Y;
    private final float minZ;
    private final float maxZ;

    private final int red;
    private final int green;
    private final int blue;
    private final int alpha;

    public BrightnessMarkerModel(MinecraftColor color, int alpha) {
        this.minX = 0.0F + ((1.0F - MARKER_SIZE) / 2.0F);
        this.maxX = 1.0F - ((1.0F - MARKER_SIZE) / 2.0F);
        this.Y = 1.0F + MARKER_OFFSET;
        this.minZ = this.minX;
        this.maxZ = this.maxX;

        this.red = color.getRed();
        this.green = color.getGreen();
        this.blue = color.getBlue();
        this.alpha = alpha;
    }

    public void draw(BrightnessMarkerRenderer renderer) {
        RenderSystem.enableBlend();
        RenderSystem.enableTexture();
        renderer.bindTexture(MARKER);
        renderer.beginVertex();
        renderer.addVertex(this.minX, this.Y, this.minZ, 0.0F, 0.0F, this.red, this.green, this.blue, this.alpha);
        renderer.addVertex(this.minX, this.Y, this.maxZ, 0.0F, 0.5F, this.red, this.green, this.blue, this.alpha);
        renderer.addVertex(this.maxX, this.Y, this.maxZ, 0.5F, 0.5F, this.red, this.green, this.blue, this.alpha);
        renderer.addVertex(this.maxX, this.Y, this.minZ, 0.5F, 0.0F, this.red, this.green, this.blue, this.alpha);
        renderer.endVertex();
        RenderSystem.disableTexture();
        RenderSystem.disableBlend();
    }
}
