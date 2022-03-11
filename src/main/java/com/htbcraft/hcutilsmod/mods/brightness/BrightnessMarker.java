package com.htbcraft.hcutilsmod.mods.brightness;

import com.htbcraft.hcutilsmod.common.MinecraftColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

public class BrightnessMarker {
    private final BrightnessMarkerModel markerModel;
    private final BlockPos pos;

    public BrightnessMarker(MinecraftColor color, int alpha, BlockPos pos) {
        this.markerModel = new BrightnessMarkerModel(color, alpha);
        this.pos = pos;
    }

    public void draw(BrightnessMarkerRenderer renderer) {
        Vector3d viewerPos = renderer.getCameraViewPosition();
        renderer.push();
        renderer.translate(
                (float)(this.pos.getX() - viewerPos.x),
                (float)(this.pos.getY() - viewerPos.y - 1.0D),
                (float)(this.pos.getZ() - viewerPos.z));
        this.markerModel.draw(renderer);
        renderer.pop();
    }
}
