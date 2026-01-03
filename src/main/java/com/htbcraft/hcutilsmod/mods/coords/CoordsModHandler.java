package com.htbcraft.hcutilsmod.mods.coords;

import com.htbcraft.hcutilsmod.HCUtilsMod;
import com.htbcraft.hcutilsmod.common.HCSettings;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// 仕様
// 登録キー押下でプレイヤーの現在座標を画面に表示する
// F3表示中はかぶってしまうので非表示
@Mod(value = HCUtilsMod.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = HCUtilsMod.MODID, value = Dist.CLIENT)
public class CoordsModHandler {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final CoordsOverlayGui coordsOverlayGui = new CoordsOverlayGui();

    public CoordsModHandler(ModContainer container) {
    }

    @SubscribeEvent
    public static void onRenderGuiPost(RenderGuiEvent.Post event) {
        if ((!Minecraft.getInstance().getDebugOverlay().showDebugScreen()) &&
                (HCSettings.getInstance().enableCordsMod)) {
            coordsOverlayGui.render(event.getGuiGraphics());
        }
    }
}
