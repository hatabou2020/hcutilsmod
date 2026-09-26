package com.htbcraft.hcutilsmod.mods.brightness;

import com.htbcraft.hcutilsmod.common.MinecraftColor;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ARGB;
import net.minecraft.world.phys.Vec3;

public class BrightnessMarker {
    private final int argb;
    private final BlockPos pos;

    public BrightnessMarker(MinecraftColor color, int alpha, BlockPos pos) {
        this.argb = ARGB.color(alpha, color.getRed(), color.getGreen(), color.getBlue());
        this.pos = pos;
    }

    public void draw(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, Vec3 cameraPosition) {
        poseStack.pushPose();
        poseStack.translate(
                pos.getX() - cameraPosition.x,
                pos.getY() - cameraPosition.y,
                pos.getZ() - cameraPosition.z);
        submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.debugFilledBox(), (pose, buffer) -> {
            float inset = 0.05F;
            float y = 0.01F;
            vertex(buffer, pose, inset, y, inset);
            vertex(buffer, pose, 1 - inset, y, inset);
            vertex(buffer, pose, 1 - inset, y, 1 - inset);
            vertex(buffer, pose, inset, y, 1 - inset);
        });
        poseStack.popPose();
    }

    private void vertex(VertexConsumer buffer, PoseStack.Pose pose, float x, float y, float z) {
        buffer.addVertex(pose, x, y, z).setColor(argb);
    }
}
