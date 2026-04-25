package com.htbcraft.hcutilsmod.mods.bed;

import com.htbcraft.hcutilsmod.HcUtilsMod;
import com.htbcraft.hcutilsmod.common.ModSounds;
import com.htbcraft.hcutilsmod.config.Config;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@Mod(value = HcUtilsMod.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = HcUtilsMod.MODID, value = Dist.CLIENT)
public class BedChimeClient {
    private static boolean isFire = false;

    public BedChimeClient(ModContainer container) {
    }

    @SubscribeEvent
    public static void onPlayerTickPost(PlayerTickEvent.Post event) {
        if (!Config.BED_CHIME_ENABLE.get()) {
            return;
        }
        if (event.getEntity() instanceof LocalPlayer player) {
            try (Level level = player.level()) {
                long dayTime = level.getOverworldClockTime() % 24000;

                // 眠ることが可能な時間帯（12541～23458）
                if (dayTime >= 12541 && dayTime < 23458) {
                    if (!isFire) {
                        HcUtilsMod.LOGGER.info("Player can sleep now! Day time: {}", dayTime);

                        // チャイムを再生
                        level.playSound(
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
            catch (Exception e) {
                HcUtilsMod.LOGGER.error("Error while trying to connect to bedchime server", e);
            }
        }
    }
}
