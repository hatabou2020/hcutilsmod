package com.htbcraft.hcutilsmod;

import com.htbcraft.hcutilsmod.common.HCKeyBinding;
import com.htbcraft.hcutilsmod.common.HCSettings;
import com.htbcraft.hcutilsmod.mods.brightness.BrightnessModHandler;
import com.htbcraft.hcutilsmod.mods.coords.CoordsModHandler;
import com.htbcraft.hcutilsmod.mods.direction.BlockDirectionModHandler;
import com.htbcraft.hcutilsmod.mods.inventory.InventoryCustomModHandler;
import com.htbcraft.hcutilsmod.mods.spawner.FindSpawnerModHandler;
import com.htbcraft.hcutilsmod.screen.MainSettingsScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.client.event.ScreenOpenEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_H;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

@Mod("hcutilsmod")
public class HCUtilsMod {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "hcutilsmod";

    private final HCSettings settings;
    private MainSettingsScreen mainSettingsScreen;

    // デフォルトキー：[H]
    private static final HCKeyBinding BIND_KEY = new HCKeyBinding(
            "hcutilsmod.settings.key_description",
            GLFW_KEY_H,
            0,
            GLFW_RELEASE
    );

    static {
        ClientRegistry.registerKeyBinding(BIND_KEY);
    }

    public HCUtilsMod() {
        settings = new HCSettings(Minecraft.getInstance());
        settings.loadOptions();

        MinecraftForge.EVENT_BUS.register(this);

        // MODのハンドラを登録
        MinecraftForge.EVENT_BUS.register(new CoordsModHandler());
        MinecraftForge.EVENT_BUS.register(new BlockDirectionModHandler());
        MinecraftForge.EVENT_BUS.register(new InventoryCustomModHandler());
        MinecraftForge.EVENT_BUS.register(new FindSpawnerModHandler());
        MinecraftForge.EVENT_BUS.register(new BrightnessModHandler());
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event) {
        LOGGER.info("Bye-Bye from server stopping");
        settings.saveOptions();
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Minecraft.getInstance().screen != null) {
            LOGGER.info("Displaying on screen");
            return;
        }

        int key = event.getKey();
        int modifiers = event.getModifiers();
        int action = event.getAction();

        // 登録キー 押下
        if (BIND_KEY.test(key, modifiers, action)) {
            if (Minecraft.getInstance().screen == null) {
                if (mainSettingsScreen == null) {
                    Minecraft.getInstance().setScreen(new MainSettingsScreen());
                }
            }
            else {
                if (mainSettingsScreen != null) {
                    Minecraft.getInstance().setScreen(null);
                }
            }
        }
    }

    @SubscribeEvent
    public void onGuiOpen(ScreenOpenEvent event) {
        Screen gui = event.getScreen();
        if (gui == null) {
            LOGGER.info("gui == null");
            mainSettingsScreen = null;
            return;
        }

        if (gui instanceof MainSettingsScreen) {
            mainSettingsScreen = (MainSettingsScreen)gui;
        }
        else {
            mainSettingsScreen = null;
        }
    }
}
