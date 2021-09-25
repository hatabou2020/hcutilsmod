package com.htbcraft.hcutilsmod.mods.direction;

import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;

public class Rotation4DirectionAdapter implements IBlockDirection {
    private BlockState blockState;

    public Rotation4DirectionAdapter(BlockState blockState) {
        this.blockState = blockState;
    }

    @Override
    public BlockState change() {
        return blockState.rotate(Rotation.CLOCKWISE_90);
    }
}
