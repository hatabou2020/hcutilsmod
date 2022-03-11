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
        Integer rot = blockState.get(BlockStateProperties.ROTATION_0_15);
        blockState = blockState.with(BlockStateProperties.ROTATION_0_15, (rot + 1) % 16);
        return blockState;
    }
}
