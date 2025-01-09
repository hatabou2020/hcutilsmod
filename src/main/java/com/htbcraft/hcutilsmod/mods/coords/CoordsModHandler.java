package com.htbcraft.hcutilsmod.mods.coords;

import com.htbcraft.hcutilsmod.common.HCSettings;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// 仕様
// 登録キー押下でプレイヤーの現在座標を画面に表示する
// F3表示中はかぶってしまうので非表示
public class CoordsModHandler {
    private static final Logger LOGGER = LogManager.getLogger();

    private final CoordsOverlayGui coordsOverlayGui;

    public CoordsModHandler() {
        coordsOverlayGui = new CoordsOverlayGui(Minecraft.getInstance());
    }

    @SubscribeEvent
    public void onRenderGuiPost(RenderGuiEvent.Post event) {
        if ((!Minecraft.getInstance().getDebugOverlay().showDebugScreen()) &&
                (HCSettings.getInstance().enableCordsMod)) {
            coordsOverlayGui.render(event.getGuiGraphics());
        }
    }
}
