package com.htbcraft.hcutilsmod.mods.direction;

import net.minecraft.block.BlockState;
import net.minecraft.util.Rotation;

public class Rotation4DirectionAdapter implements IBlockDirection {
    private BlockState blockState;

    public Rotation4DirectionAdapter(BlockState blockState) {
        this.blockState = blockState;
    }

    @Override
    public BlockState change() {
        blockState = blockState.rotate(Rotation.CLOCKWISE_90);
        return blockState;
    }
}
