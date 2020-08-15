package com.htbcraft.hcutilsmod.mods.spawner;

import com.htbcraft.hcutilsmod.common.HCSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
                BlockPos pos = event.player.getPosition();
                if (prevPlayerPos.compareTo(pos) != 0) {
                    prevPlayerPos = pos;

                    // 1マス歩くごとに検索
                    new Thread(new FindSpawnerPos(event.player.world, event.player, HCSettings.getInstance().rangeFindSpawner)).start();
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
                Minecraft.getInstance().getToastGui().add(new FindSpawnerToast(hitBlockPos));
                prevHitBlockPos = hitBlockPos;
            }
        }
        else {
            if ((hitBlockPos != null) && (hitBlockPos.compareTo(prevHitBlockPos) != 0)) {
                // 別のスポナーを見つけたとき
                Minecraft.getInstance().getToastGui().add(new FindSpawnerToast(hitBlockPos));
            }

            prevHitBlockPos = hitBlockPos;
        }
    }

    private class FindSpawnerPos extends BlockPos.MutableBlockPos implements Runnable {
        private final World world;
        private final PlayerEntity player;
        private final int renge;

        public FindSpawnerPos(World world, PlayerEntity player, int renge) {
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
            BlockPos pos = this.player.getPosition();

            int i = pos.getX() - this.renge;
            int j = pos.getX() + this.renge;
            int k = pos.getY() - this.renge;
            int l = pos.getY() + this.renge;
            int i1 = pos.getZ() - this.renge;
            int j1 = pos.getZ() + this.renge;

            for (int k1 = i; k1 < j; ++k1) {
                for (int l1 = k; l1 < l; ++l1) {
                    for (int i2 = i1; i2 < j1; ++i2) {
                        BlockState blockstate = this.world.getBlockState(this.setPos(k1, l1, i2));
                        if (blockstate.getBlock() == Blocks.SPAWNER) {
                            return this.toImmutable();
                        }
                    }
                }
            }

            return null;
        }
    }
}
