package com.htbcraft.hcutilsmod;

import com.htbcraft.hcutilsmod.mods.coords.CoordsModHandler;
import com.htbcraft.hcutilsmod.mods.direction.BlockDirectionModHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("hcutilsmod")
public class HCUtilsMod {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "hcutilsmod";

    public HCUtilsMod() {
        // MODのハンドラを登録
        MinecraftForge.EVENT_BUS.register(new BlockDirectionModHandler());
        MinecraftForge.EVENT_BUS.register(new CoordsModHandler());
    }
}
