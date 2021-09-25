package com.htbcraft.hcutilsmod.mods.direction;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.ChestType;

public class ChestDirectionAdapter implements IBlockDirection {
    private BlockState blockState;

    public ChestDirectionAdapter(BlockState blockState) {
        this.blockState = blockState;
    }

    @Override
    public BlockState change() {
        // シングルチェストだけ
        if (ChestType.SINGLE.equals(blockState.getValue(BlockStateProperties.CHEST_TYPE))) {
            blockState = new HorizontalDirectionAdapter(blockState).change();
        }

        return blockState;
    }
}
