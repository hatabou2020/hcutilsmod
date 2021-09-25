package com.htbcraft.hcutilsmod.mods.spawner;

import com.htbcraft.hcutilsmod.common.HCSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

public class FindSpawnerModHandler {
    private static final Logger LOGGER = LogManager.getLogger();

    private BlockPos prevPlayerPos = BlockPos.ZERO;
    private BlockPos hitBlockPos = null;
    private BlockPos prevHitBlockPos = null;

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side.isClient()) {
            if (HCSettings.getInstance().enableFindSpawnerMod) {
                BlockPos pos = event.player.blockPosition();
                if (prevPlayerPos.compareTo(pos) != 0) {
                    prevPlayerPos = pos;

                    // 1マス歩くごとに検索
                    new Thread(new FindSpawnerPos(event.player.level, event.player, HCSettings.getInstance().rangeFindSpawner)).start();
                }
            }
        }

        // 設定がオフになったらリセットする
        if (!HCSettings.getInstance().enableFindSpawnerMod) {
            prevHitBlockPos = null;
            hitBlockPos = null;
        }

        if (prevHitBlockPos == null) {
            if (hitBlockPos != null) {
                // スポナーを見つけたらトースト表示
                Minecraft.getInstance().getToasts().addToast(new FindSpawnerToast(hitBlockPos));
                prevHitBlockPos = hitBlockPos;
            }
        }
        else {
            if ((hitBlockPos != null) && (hitBlockPos.compareTo(prevHitBlockPos) != 0)) {
                // 別のスポナーを見つけたとき
                Minecraft.getInstance().getToasts().addToast(new FindSpawnerToast(hitBlockPos));
            }

            prevHitBlockPos = hitBlockPos;
        }
    }

    private class FindSpawnerPos extends BlockPos.MutableBlockPos implements Runnable {
        private final Level world;
        private final Player player;
        private final int renge;

        public FindSpawnerPos(Level world, Player player, int renge) {
            super(0, 0, 0);
            this.world = world;
            this.player = player;
            this.renge = renge;
        }

        @Override
        public void run() {
            FindSpawnerModHandler.this.hitBlockPos = findSpawnerPosInArea();
        }

        @Nullable
        public BlockPos findSpawnerPosInArea() {
            BlockPos pos = this.player.blockPosition();

            int i = pos.getX() - this.renge;
            int j = pos.getX() + this.renge;
            int k = pos.getY() - this.renge;
            int l = pos.getY() + this.renge;
            int i1 = pos.getZ() - this.renge;
            int j1 = pos.getZ() + this.renge;

            for (int k1 = i; k1 < j; ++k1) {
                for (int l1 = k; l1 < l; ++l1) {
                    for (int i2 = i1; i2 < j1; ++i2) {
                        BlockState blockstate = this.world.getBlockState(this.set(k1, l1, i2));
                        if (blockstate.getBlock() == Blocks.SPAWNER) {
                            return this.immutable();
                        }
                    }
                }
            }

            return null;
        }
    }
}
