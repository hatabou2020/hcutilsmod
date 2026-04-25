package com.htbcraft.hcutilsmod;

import com.htbcraft.hcutilsmod.config.HcUtilsModSettingsScreen;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = HcUtilsMod.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = HcUtilsMod.MODID, value = Dist.CLIENT)
public class HcUtilsModClient {
    public HcUtilsModClient(ModContainer container) {
        container.registerExtensionPoint(
                IConfigScreenFactory.class,
                (mc, parent) -> new HcUtilsModSettingsScreen(parent));
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        HcUtilsMod.LOGGER.info("HELLO FROM CLIENT SETUP");
        HcUtilsMod.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
    }
}
