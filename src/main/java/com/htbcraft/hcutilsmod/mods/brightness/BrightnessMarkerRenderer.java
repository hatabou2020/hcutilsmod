package com.htbcraft.hcutilsmod.mods.brightness;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.phys.Vec3;

public class BrightnessMarkerRenderer {
    private final Minecraft minecraft;

    private PoseStack matrixStack;
    private EntityRenderDispatcher renderManager;

    private BufferBuilder bufferBuilder;

    public BrightnessMarkerRenderer(Minecraft minecraft) {
        this.minecraft = minecraft;
    }

    public BrightnessMarkerRenderer update(PoseStack matrixStack) {
        this.matrixStack = matrixStack;
        this.renderManager = this.minecraft.getEntityRenderDispatcher();
        return this;
    }

    public Vec3 getCameraViewPosition() {
        return this.renderManager.camera.getPosition();
    }

    public void beginVertex() {
        this.bufferBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
    }

    public void endVertex() {
        BufferUploader.drawWithShader(this.bufferBuilder.buildOrThrow());
    }

    public void addVertex(float x, float y, float z, float u, float v, int r, int g, int b, int alpha) {
        this.bufferBuilder
                .addVertex(this.matrixStack.last().pose(), x, y, z)
                .setUv(u, v)
                .setColor(r, g, b, alpha);
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
