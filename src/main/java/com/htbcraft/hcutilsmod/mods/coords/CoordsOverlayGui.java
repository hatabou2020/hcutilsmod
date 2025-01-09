package com.htbcraft.hcutilsmod.mods.coords;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CoordsOverlayGui {
    private static final Logger LOGGER = LogManager.getLogger();

    private final Minecraft mc;
    private BlockPos oldBlockPos = null;
    private String textCoords = "";

    public CoordsOverlayGui(Minecraft mc) {
        this.mc = mc;
    }

    public void render(GuiGraphics guiGraphics) {
        Entity cameraEntity = this.mc.getCameraEntity();
        if (cameraEntity == null) {
            return;
        }

        BlockPos blockpos = cameraEntity.blockPosition();

        if ((oldBlockPos == null) || !oldBlockPos.equals(blockpos)) {
            oldBlockPos = blockpos;
            textCoords = "X:" + blockpos.getX() + " Y:" + blockpos.getY() + " Z:" + blockpos.getZ();
            LOGGER.info(textCoords);
        }

        int w = this.mc.font.width(textCoords);
        int h = this.mc.font.lineHeight;
        guiGraphics.fill(1, 1, 1 + w + 1, 1 + h + 1, -1873784752);
        guiGraphics.drawString(this.mc.font, textCoords, 2, 2, 14737632, false);
    }
}
