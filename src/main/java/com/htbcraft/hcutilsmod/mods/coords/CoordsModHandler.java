package com.htbcraft.hcutilsmod.mods.coords;

import com.htbcraft.hcutilsmod.common.HCSettings;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// 仕様
// 登録キー押下でプレイヤーの現在座標を画面に表示する
// F3表示中はかぶってしまうので非表示
public class CoordsModHandler {
    private static final Logger LOGGER = LogManager.getLogger();

    private final CoordsOverlayGui coordsOverlayGui;

    public CoordsModHandler() {
        coordsOverlayGui = new CoordsOverlayGui();
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side.isClient()) {
            if ((!Minecraft.getInstance().gameSettings.showDebugInfo) && (HCSettings.getInstance().enableCordsMod)) {
                // 座標の更新
                coordsOverlayGui.setBlockPos(event.player.func_233580_cy_());
            }
        }
    }

    @SubscribeEvent
    public void onRenderGameOverlayText(RenderGameOverlayEvent.Text event) {
        if ((!Minecraft.getInstance().gameSettings.showDebugInfo) && (HCSettings.getInstance().enableCordsMod)) {
            coordsOverlayGui.render(event.getMatrixStack(), Minecraft.getInstance().fontRenderer);
        }
    }
}
