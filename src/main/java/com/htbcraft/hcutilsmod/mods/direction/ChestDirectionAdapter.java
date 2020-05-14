package com.htbcraft.hcutilsmod.mods.direction;

import net.minecraft.block.BlockState;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.ChestType;

public class ChestDirectionAdapter implements IBlockDirection {
    private BlockState blockState;

    public ChestDirectionAdapter(BlockState blockState) {
        this.blockState = blockState;
    }

    @Override
    public BlockState change() {
        // シングルチェストだけ
        if (ChestType.SINGLE.equals(blockState.get(BlockStateProperties.CHEST_TYPE))) {
            blockState = new HorizontalDirectionAdapter(blockState).change();
        }

        return blockState;
    }
}
