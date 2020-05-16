package com.htbcraft.hcutilsmod.mods.coords;

import com.htbcraft.hcutilsmod.common.HCKeyBinding;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static org.lwjgl.glfw.GLFW.*;

// 仕様
// 登録キー押下でプレイヤーの現在座標を画面に表示する
// F3表示中はかぶってしまうので非表示
public class CoordsModHandler {
    private static final Logger LOGGER = LogManager.getLogger();

    private boolean enableDsiplay = false;
    private boolean prevDsiplay = false;
    private boolean showDebugInfo = false;

    private final CoordsOverlayGui coordsOverlayGui;

    // デフォルトキー：[F4]
    private static final HCKeyBinding BIND_KEY = new HCKeyBinding(
        "hcutilsmod.coords.key_description",
        GLFW_KEY_F4,
        0,
        GLFW_RELEASE
    );

    static {
        ClientRegistry.registerKeyBinding(BIND_KEY);
    }

    public CoordsModHandler() {
        coordsOverlayGui = new CoordsOverlayGui();
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        int key = event.getKey();
        int modifiers = event.getModifiers();
        int action = event.getAction();

        // 登録キー 押下
        if (BIND_KEY.test(key, modifiers, action)) {
            if (!showDebugInfo) {
                enableDsiplay = !enableDsiplay;
            }
        }

        if (showDebugInfo != Minecraft.getInstance().gameSettings.showDebugInfo) {
            showDebugInfo = Minecraft.getInstance().gameSettings.showDebugInfo;

            // F3表示中は消す
            if (showDebugInfo) {
                prevDsiplay = enableDsiplay;
                enableDsiplay = false;
            }
            else {
                // F3表示する前の状態に
                enableDsiplay = prevDsiplay;
            }
        }
    }

    @SubscribeEvent
    public void onPlayer(PlayerEvent event) {
        if (enableDsiplay) {
            PlayerEntity player = event.getPlayer();
            if (player == null) {
                return;
            }

            // 座標の更新
            coordsOverlayGui.setBlockPos(player.getPosition());
        }
    }

    @SubscribeEvent
    public void onRenderGameOverlayText(RenderGameOverlayEvent.Text event) {
        if (enableDsiplay) {
            coordsOverlayGui.render(Minecraft.getInstance().fontRenderer);
        }
    }
}
