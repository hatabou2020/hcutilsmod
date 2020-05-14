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
        Direction direction = blockState.get(BlockStateProperties.FACING_EXCEPT_UP);

        if (Direction.NORTH.equals(direction)) {
            blockState = blockState.with(BlockStateProperties.FACING_EXCEPT_UP, Direction.EAST);
        }
        else if (Direction.EAST.equals(direction)) {
            blockState = blockState.with(BlockStateProperties.FACING_EXCEPT_UP, Direction.SOUTH);
        }
        else if (Direction.SOUTH.equals(direction)) {
            blockState = blockState.with(BlockStateProperties.FACING_EXCEPT_UP, Direction.WEST);
        }
        else if (Direction.WEST.equals((direction))) {
            blockState = blockState.with(BlockStateProperties.FACING_EXCEPT_UP, Direction.DOWN);
        }
        else {
            blockState = blockState.with(BlockStateProperties.FACING_EXCEPT_UP, Direction.NORTH);
        }

        return blockState;
    }
}
