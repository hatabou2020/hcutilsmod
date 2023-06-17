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
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
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

    public void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(BIND_KEY);
    }

    public HCUtilsMod() {
        settings = new HCSettings(Minecraft.getInstance());
        settings.loadOptions();

        CoordsModHandler coordsModHandler = new CoordsModHandler();
        BlockDirectionModHandler blockDirectionModHandler = new BlockDirectionModHandler();
        InventoryCustomModHandler inventoryCustomModHandler = new InventoryCustomModHandler();
        FindSpawnerModHandler findSpawnerModHandler = new FindSpawnerModHandler();
        BrightnessModHandler brightnessModHandler = new BrightnessModHandler();

        // キー登録のハンドラを登録
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::onRegisterKeyMappings);
        modEventBus.addListener(blockDirectionModHandler::onRegisterKeyMappings);
        modEventBus.addListener(inventoryCustomModHandler::onRegisterKeyMappings);
//        modEventBus.addListener(brightnessModHandler::onRegisterKeyMappings);

        // MODのハンドラを登録
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(coordsModHandler);
        MinecraftForge.EVENT_BUS.register(blockDirectionModHandler);
        MinecraftForge.EVENT_BUS.register(inventoryCustomModHandler);
        MinecraftForge.EVENT_BUS.register(findSpawnerModHandler);
//        MinecraftForge.EVENT_BUS.register(brightnessModHandler);
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
