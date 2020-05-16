package com.htbcraft.hcutilsmod.mods.coords;

import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CoordsOverlayGui extends AbstractGui {
    private static final Logger LOGGER = LogManager.getLogger();

    private BlockPos blockPos = null;
    private String textCoords = "";

    public void setBlockPos(BlockPos pos) {
        if ((blockPos == null) || !blockPos.equals(pos)) {
            blockPos = pos;
            textCoords = "X:" + pos.getX() + " Y:" + pos.getY() + " Z:" + pos.getZ();
            LOGGER.info(pos.toString());
        }
    }

    public void render(FontRenderer fontRenderer) {
        int j = 9;
        int k = fontRenderer.getStringWidth(textCoords);
        int i1 = 2;
        fill(1, i1 - 1, 2 + k + 1, i1 + j - 1, -1873784752);
        fontRenderer.drawStringWithShadow(textCoords, 2.0F, (float)i1, 14737632);
    }
}
