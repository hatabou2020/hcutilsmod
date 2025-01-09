package com.htbcraft.hcutilsmod.mods.spawner;

import com.htbcraft.hcutilsmod.common.HCSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FindSpawnerModHandler {
    private static final Logger LOGGER = LogManager.getLogger();

    private BlockPos prevPlayerPos = BlockPos.ZERO;
    private BlockPos prevHitBlockPos = BlockPos.ZERO;
    private BlockPos hitBlockPos = null;

    @SubscribeEvent
    public void onPlayerTickPost(PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof ServerPlayer) {
            return;
        }

        if (HCSettings.getInstance().enableFindSpawnerMod) {
            BlockPos playerPos = event.getEntity().blockPosition();
            if (prevPlayerPos.compareTo(playerPos) != 0) {
                prevPlayerPos = playerPos;

                // 1マス歩くごとに検索
                new Thread(() ->
                        hitBlockPos = findSpawnerPosInArea(
                            event.getEntity().level(),
                            playerPos,
                            HCSettings.getInstance().rangeFindSpawner)
                ).start();
            }
        }
        else {
            // 設定がオフになったらリセットする
            prevPlayerPos = BlockPos.ZERO;
            prevHitBlockPos = BlockPos.ZERO;
            hitBlockPos = null;
        }

        // スポナーを見つけたらトースト表示
        if (hitBlockPos != null) {
            if (prevHitBlockPos.compareTo(hitBlockPos) != 0) {
                LOGGER.info("Add Toast!! " + hitBlockPos);
                Minecraft.getInstance().getToastManager().addToast(
                        new FindSpawnerToast(
                                hitBlockPos,
                                HCSettings.getInstance().timeFindSpawner));
                prevHitBlockPos = hitBlockPos;
            }
        }
        else {
            prevHitBlockPos = BlockPos.ZERO;
        }
    }

    private BlockPos findSpawnerPosInArea(Level world, BlockPos pos, int range) {
        int i = pos.getX() - range;
        int j = pos.getX() + range;
        int k = pos.getY() - range;
        int l = pos.getY() + range;
        int i1 = pos.getZ() - range;
        int j1 = pos.getZ() + range;

        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();

        for (int k1 = i; k1 < j; ++k1) {
            for (int l1 = k; l1 < l; ++l1) {
                for (int i2 = i1; i2 < j1; ++i2) {
                    BlockState blockstate = world.getBlockState(mutableBlockPos.set(k1, l1, i2));
                    if (blockstate.getBlock() == Blocks.SPAWNER) {
                        return mutableBlockPos.immutable();
                    }
                }
            }
        }

        return null;
    }
}
