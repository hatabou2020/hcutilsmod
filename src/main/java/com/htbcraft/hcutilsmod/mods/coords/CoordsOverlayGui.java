package com.htbcraft.hcutilsmod.mods.coords;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CoordsOverlayGui {
    private static final Logger LOGGER = LogManager.getLogger();

    private BlockPos oldBlockPos = null;
    private String textCoords = "";

    public CoordsOverlayGui() {
    }

    public void render(GuiGraphics guiGraphics) {
        Entity cameraEntity = Minecraft.getInstance().getCameraEntity();
        if (cameraEntity == null) {
            return;
        }

        BlockPos blockpos = cameraEntity.blockPosition();

        if ((oldBlockPos == null) || !oldBlockPos.equals(blockpos)) {
            oldBlockPos = blockpos;
            textCoords = "X:" + blockpos.getX() + " Y:" + blockpos.getY() + " Z:" + blockpos.getZ();
            LOGGER.info(textCoords);
        }

        int w = Minecraft.getInstance().font.width(textCoords);
        int h = Minecraft.getInstance().font.lineHeight;
        guiGraphics.fill(1, 1, 1 + w + 1, 1 + h + 1, -1873784752);
        guiGraphics.drawString(Minecraft.getInstance().font, textCoords, 2, 2, -2039584, false);
    }
}
