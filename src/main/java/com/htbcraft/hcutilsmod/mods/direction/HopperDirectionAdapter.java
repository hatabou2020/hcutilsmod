package com.htbcraft.hcutilsmod.mods.direction;

import net.minecraft.block.BlockState;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;

public class HopperDirectionAdapter implements IBlockDirection {
    private BlockState blockState;

    public HopperDirectionAdapter(BlockState blockState) {
        this.blockState = blockState;
    }

    @Override
    public BlockState change() {
        Direction direction = blockState.getValue(BlockStateProperties.FACING_HOPPER);

        if (Direction.NORTH.equals(direction)) {
            blockState = blockState.setValue(BlockStateProperties.FACING_HOPPER, Direction.EAST);
        }
        else if (Direction.EAST.equals(direction)) {
            blockState = blockState.setValue(BlockStateProperties.FACING_HOPPER, Direction.SOUTH);
        }
        else if (Direction.SOUTH.equals(direction)) {
            blockState = blockState.setValue(BlockStateProperties.FACING_HOPPER, Direction.WEST);
        }
        else if (Direction.WEST.equals((direction))) {
            blockState = blockState.setValue(BlockStateProperties.FACING_HOPPER, Direction.DOWN);
        }
        else {
            blockState = blockState.setValue(BlockStateProperties.FACING_HOPPER, Direction.NORTH);
        }

        return blockState;
    }
}
