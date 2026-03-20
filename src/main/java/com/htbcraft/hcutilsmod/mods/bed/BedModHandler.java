package com.htbcraft.hcutilsmod.mods.bed;

import com.htbcraft.hcutilsmod.HCUtilsMod;
import com.htbcraft.hcutilsmod.ModSounds;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(value = HCUtilsMod.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = HCUtilsMod.MODID, value = Dist.CLIENT)
public class BedModHandler {
    private static final Logger LOGGER = LogManager.getLogger();

    private static boolean isFire = false;

    public BedModHandler(ModContainer container) {
    }

    @SubscribeEvent
    public static void onPlayerTickPost(PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof LocalPlayer player) {
            long dayTime = player.level().getDayTime() % 24000;
            
            // 眠ることが可能な時間帯（12541～23458）
            if (dayTime >= 12541 && dayTime < 23458) {
                if (!isFire) {
                    LOGGER.info("Player can sleep now! Day time: {}", dayTime);
                    
                    // チャイムを再生
                    player.level().playSound(
                        player,
                        player.getX(),
                        player.getY(),
                        player.getZ(),
                        ModSounds.BED_CHIME.value(),
                        SoundSource.MASTER,
                        1.0f,
                        1.0f
                    );
                }
                isFire = true;
            }
            else {
                isFire = false;
            }
        }
    }
}
