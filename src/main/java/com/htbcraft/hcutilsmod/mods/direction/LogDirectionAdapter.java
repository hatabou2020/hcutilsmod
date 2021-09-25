package com.htbcraft.hcutilsmod.mods.direction;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class LogDirectionAdapter implements IBlockDirection {
    private BlockState blockState;

    public LogDirectionAdapter(BlockState blockState) {
        this.blockState = blockState;
    }

    @Override
    public BlockState change() {
        Direction.Axis axis = blockState.getValue(BlockStateProperties.AXIS);

        if (Direction.Axis.X.equals(axis)) {
            blockState = blockState.setValue(BlockStateProperties.AXIS, Direction.Axis.Z);
        }
        else if (Direction.Axis.Z.equals(axis)) {
            blockState = blockState.setValue(BlockStateProperties.AXIS, Direction.Axis.Y);
        }
        else {
            blockState = blockState.setValue(BlockStateProperties.AXIS, Direction.Axis.X);
        }

        return blockState;
    }
}
