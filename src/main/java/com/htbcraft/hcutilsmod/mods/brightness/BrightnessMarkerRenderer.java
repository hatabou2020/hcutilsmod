package com.htbcraft.hcutilsmod.mods.brightness;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class BrightnessMarkerRenderer {
    private final Minecraft minecraft;

    private PoseStack matrixStack;
    private EntityRenderDispatcher renderManager;
    private TextureManager textureManager;

    public BrightnessMarkerRenderer(Minecraft minecraft) {
        this.minecraft = minecraft;
    }

    private Tesselator tesselator() {
        return Tesselator.getInstance();
    }

    private BufferBuilder buffer() {
        return tesselator().getBuilder();
    }

    public BrightnessMarkerRenderer update(PoseStack matrixStack) {
        this.matrixStack = matrixStack;
        this.renderManager = this.minecraft.getEntityRenderDispatcher();
        this.textureManager = this.minecraft.getTextureManager();
        return this;
    }

    public Vec3 getCameraViewPosition() {
        return this.renderManager.camera.getPosition();
    }

    public void beginVertex() {
        buffer().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
    }

    public void endVertex() {
        tesselator().end();
    }

    public void addVertex(float x, float y, float z, float u, float v, int r, int g, int b, int alpha) {
        buffer().vertex(this.matrixStack.last().pose(), x, y, z).color(r, g, b, alpha).uv(u, v).endVertex();
    }

    public void bindTexture(ResourceLocation texture) {
        this.textureManager.bindForSetup(texture);
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
