package com.htbcraft.hcutilsmod.mods.direction;

import net.minecraft.block.BlockState;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;

public class LogDirectionAdapter implements IBlockDirection {
    private BlockState blockState;

    public LogDirectionAdapter(BlockState blockState) {
        this.blockState = blockState;
    }

    @Override
    public BlockState change() {
        Direction.Axis axis = blockState.get(BlockStateProperties.AXIS);

        if (Direction.Axis.X.equals(axis)) {
            blockState = blockState.with(BlockStateProperties.AXIS, Direction.Axis.Z);
        }
        else if (Direction.Axis.Z.equals(axis)) {
            blockState = blockState.with(BlockStateProperties.AXIS, Direction.Axis.Y);
        }
        else {
            blockState = blockState.with(BlockStateProperties.AXIS, Direction.Axis.X);
        }

        return blockState;
    }
}
