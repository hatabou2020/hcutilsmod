package com.htbcraft.hcutilsmod.mods.coords;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class CoordsOverlayGui extends AbstractGui {
    private static final Logger LOGGER = LogManager.getLogger();

    private final Minecraft mc;
    private BlockPos oldBlockPos = null;
    private String textCoords = "";

    public CoordsOverlayGui(Minecraft mc) {
        this.mc = mc;
    }

    public void render(MatrixStack matrixStack) {
        BlockPos blockpos = Objects.requireNonNull(this.mc.getRenderViewEntity()).getPosition();

        if ((oldBlockPos == null) || !oldBlockPos.equals(blockpos)) {
            oldBlockPos = blockpos;
            textCoords = "X:" + blockpos.getX() + " Y:" + blockpos.getY() + " Z:" + blockpos.getZ();
            LOGGER.info(textCoords);
        }

        int w = this.mc.fontRenderer.getStringWidth(textCoords);
        int h = 9;
        fill(matrixStack, 1, 1, 1 + w + 1, 1 + h + 1, -1873784752);
        this.mc.fontRenderer.drawStringWithShadow(matrixStack, textCoords, 2.0F, 2.0F, 14737632);
    }
}
