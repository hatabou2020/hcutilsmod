package com.htbcraft.hcutilsmod.mods.coords;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.core.BlockPos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class CoordsOverlayGui extends GuiComponent {
    private static final Logger LOGGER = LogManager.getLogger();

    private final Minecraft mc;
    private BlockPos oldBlockPos = null;
    private String textCoords = "";

    public CoordsOverlayGui(Minecraft mc) {
        this.mc = mc;
    }

    public void render(PoseStack matrixStack) {
        BlockPos blockpos = Objects.requireNonNull(this.mc.getCameraEntity()).blockPosition();

        if ((oldBlockPos == null) || !oldBlockPos.equals(blockpos)) {
            oldBlockPos = blockpos;
            textCoords = "X:" + blockpos.getX() + " Y:" + blockpos.getY() + " Z:" + blockpos.getZ();
            LOGGER.info(textCoords);
        }

        int w = this.mc.font.width(textCoords);
        int h = this.mc.font.lineHeight;
        fill(matrixStack, 1, 1, 1 + w + 1, 1 + h + 1, -1873784752);
        this.mc.font.draw(matrixStack, textCoords, 2.0F, 2.0F, 14737632);
    }
}
