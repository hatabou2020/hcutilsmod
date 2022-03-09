package com.htbcraft.hcutilsmod.mods.brightness;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;

public class BrightnessMarkerRenderer {
    private final Minecraft minecraft;

    private MatrixStack matrixStack;
    private EntityRendererManager renderManager;
    private TextureManager textureManager;

    public BrightnessMarkerRenderer(Minecraft minecraft) {
        this.minecraft = minecraft;
    }

    private Tessellator tesselator() {
        return Tessellator.getInstance();
    }

    private BufferBuilder buffer() {
        return tesselator().getBuilder();
    }

    public BrightnessMarkerRenderer update(MatrixStack matrixStack) {
        this.matrixStack = matrixStack;
        this.renderManager = this.minecraft.getEntityRenderDispatcher();
        this.textureManager = this.minecraft.getTextureManager();
        return this;
    }

    public Vector3d getCameraViewPosition() {
        return this.renderManager.camera.getPosition();
    }

    public void beginVertex() {
        buffer().begin(7, DefaultVertexFormats.POSITION_COLOR_TEX);
    }

    public void endVertex() {
        tesselator().end();
    }

    public void addVertex(float x, float y, float z, float u, float v, int r, int g, int b, int alpha) {
        buffer().vertex(this.matrixStack.last().pose(), x, y, z).color(r, g, b, alpha).uv(u, v).endVertex();
    }

    public void bindTexture(ResourceLocation texture) {
        this.textureManager.bind(texture);
    }

    public void push() {
        this.matrixStack.pushPose();
    }

    public void pop() {
        this.matrixStack.popPose();
    }

    public void translate(float x, float y, float z) {
        this.matrixStack.translate(x, y, z);
    }
}
