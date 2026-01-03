package com.htbcraft.hcutilsmod.mods.brightness;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ARGB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public class BrightnessMarkerRenderer {
    private PoseStack matrixStack;
    private Matrix4f matrix4f;
    private BufferBuilder bufferBuilder;

    public BrightnessMarkerRenderer() {
    }

    public BrightnessMarkerRenderer update(PoseStack matrixStack, Matrix4f matrix4f) {
        this.matrixStack = matrixStack;
        this.matrix4f = matrix4f;
        return this;
    }

    public Vec3 getCameraViewPosition() {
        Camera camera = Minecraft.getInstance().getEntityRenderDispatcher().camera;
        if (camera == null) {
            return null;
        }
        return camera.getPosition();
    }

    public void beginVertex() {
        this.bufferBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
    }

    public void endVertex() {
    }

    public void addVertex(float x, float y, float z, float u, float v, int r, int g, int b, int alpha) {
        this.bufferBuilder
                .addVertex(this.matrix4f, x, y, z)
                .setUv(u, v)
                .setColor(ARGB.color(alpha, r, g, b));
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
