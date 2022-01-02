package com.htbcraft.hcutilsmod.mods.brightness;

import com.htbcraft.hcutilsmod.common.MinecraftColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public class BrightnessMarker {
    private final BrightnessMarkerModel markerModel;
    private final BlockPos pos;

    public BrightnessMarker(MinecraftColor color, int alpha, BlockPos pos) {
        this.markerModel = new BrightnessMarkerModel(color, alpha);
        this.pos = pos;
    }

    public void draw(BrightnessMarkerRenderer renderer) {
        Vec3 viewerPos = renderer.getCameraViewPosition();
        renderer.push();
        renderer.translate(
                (float)(this.pos.getX() - viewerPos.x),
                (float)(this.pos.getY() - viewerPos.y - 1.0D),
                (float)(this.pos.getZ() - viewerPos.z));
        this.markerModel.draw(renderer);
        renderer.pop();
    }
}
