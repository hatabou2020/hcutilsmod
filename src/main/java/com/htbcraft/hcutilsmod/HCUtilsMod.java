package com.htbcraft.hcutilsmod;

import com.htbcraft.hcutilsmod.common.HCKeyBinding;
import com.htbcraft.hcutilsmod.common.HCSettings;
import com.htbcraft.hcutilsmod.screen.MainSettingsScreen;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static org.lwjgl.glfw.GLFW.*;

@Mod(HCUtilsMod.MODID)
public class HCUtilsMod {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "hcutilsmod";

    private HCSettings settings;
    private MainSettingsScreen mainSettingsScreen;

    // デフォルトキー：[H]
    private static final HCKeyBinding BIND_KEY = new HCKeyBinding(
            "key.category.minecraft.hcutilsmod.settings",
            GLFW_KEY_H,
            0,
            GLFW_RELEASE
    );

    public HCUtilsMod(IEventBus modEventBus, ModContainer modContainer) {
        // キー登録のハンドラを登録
        modEventBus.addListener(this::onRegisterKeyMappings);

        // MODのハンドラを登録
        NeoForge.EVENT_BUS.register(this);
    }

    public void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(BIND_KEY);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
        settings = new HCSettings(Minecraft.getInstance());
        settings.loadOptions();
    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event) {
        LOGGER.info("Bye-Bye from server stopping");
        settings.saveOptions();
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.Key event) {
        if ((Minecraft.getInstance().screen != null) &&
            (Minecraft.getInstance().screen != mainSettingsScreen)) {
            LOGGER.info("Displaying on screen");
            return;
        }

        int key = event.getKey();
        int modifiers = event.getModifiers();
        int action = event.getAction();

        // 登録キー 押下
        if (BIND_KEY.test(key, modifiers, action)) {
            if (mainSettingsScreen == null) {
                mainSettingsScreen = new MainSettingsScreen();
                Minecraft.getInstance().setScreen(mainSettingsScreen);
            }
            else {
                Minecraft.getInstance().setScreen(null);
            }
        }
    }

    @SubscribeEvent
    public void onScreenClosing(ScreenEvent.Closing event) {
        mainSettingsScreen = null;
    }
}
