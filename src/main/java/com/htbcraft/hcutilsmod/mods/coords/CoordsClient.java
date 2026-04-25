package com.htbcraft.hcutilsmod.mods.coords;

import com.htbcraft.hcutilsmod.HcUtilsMod;
import com.htbcraft.hcutilsmod.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

@Mod(value = HcUtilsMod.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = HcUtilsMod.MODID, value = Dist.CLIENT)
public class CoordsClient {
    private static BlockPos oldBlockPos = null;
    private static String textCoords = "";

    public CoordsClient(ModContainer container) {}

    @SubscribeEvent
    public static void onRenderGuiPost(RenderGuiEvent.Post event) {
        if (!Minecraft.getInstance().getDebugOverlay().showDebugScreen() && Config.COORDS_ENABLE.get()) {
            render(event.getGuiGraphics());
        }
    }

    private static void render(GuiGraphicsExtractor graphics) {
        Entity cameraEntity = Minecraft.getInstance().getCameraEntity();
        if (cameraEntity == null) {
            return;
        }

        BlockPos blockpos = cameraEntity.blockPosition();

        if ((oldBlockPos == null) || !oldBlockPos.equals(blockpos)) {
            oldBlockPos = blockpos;
            textCoords = "X:" + blockpos.getX() + " Y:" + blockpos.getY() + " Z:" + blockpos.getZ();
            HcUtilsMod.LOGGER.info(textCoords);
        }

        int w = Minecraft.getInstance().font.width(textCoords);
        int h = Minecraft.getInstance().font.lineHeight;
        graphics.fill(1, 1, 1 + w + 1, 1 + h + 1, -1873784752);
        graphics.text(Minecraft.getInstance().font, textCoords, 2, 2, -2039584, false);
    }
}
