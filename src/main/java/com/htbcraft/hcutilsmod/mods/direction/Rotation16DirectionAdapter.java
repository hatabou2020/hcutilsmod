package com.htbcraft.hcutilsmod.mods.direction;

import net.minecraft.block.BlockState;
import net.minecraft.state.properties.BlockStateProperties;

public class Rotation16DirectionAdapter implements IBlockDirection {
    private BlockState blockState;

    public Rotation16DirectionAdapter(BlockState blockState) {
        this.blockState = blockState;
    }

    @Override
    public BlockState change() {
        Integer rot = blockState.getValue(BlockStateProperties.ROTATION_16);
        return blockState.setValue(BlockStateProperties.ROTATION_16, (rot + 1) % 16);
    }
}
