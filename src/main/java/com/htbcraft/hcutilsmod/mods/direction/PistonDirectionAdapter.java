package com.htbcraft.hcutilsmod.mods.direction;

import net.minecraft.block.BlockState;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;

public class PistonDirectionAdapter implements IBlockDirection {
    private BlockState blockState;

    public PistonDirectionAdapter(BlockState blockState) {
        this.blockState = blockState;
    }

    @Override
    public BlockState change() {
        // 伸びた状態のピストンは回転するとバグる
        if (!blockState.getValue(BlockStateProperties.EXTENDED)) {
            Direction direction = blockState.getValue(BlockStateProperties.FACING);

            if (Direction.NORTH.equals(direction)) {
                blockState = blockState.setValue(BlockStateProperties.FACING, Direction.EAST);
            }
            else if (Direction.EAST.equals(direction)) {
                blockState = blockState.setValue(BlockStateProperties.FACING, Direction.SOUTH);
            }
            else if (Direction.SOUTH.equals(direction)) {
                blockState = blockState.setValue(BlockStateProperties.FACING, Direction.WEST);
            }
            else if (Direction.WEST.equals((direction))) {
                blockState = blockState.setValue(BlockStateProperties.FACING, Direction.UP);
            }
            else if (Direction.UP.equals((direction))) {
                blockState = blockState.setValue(BlockStateProperties.FACING, Direction.DOWN);
            }
            else {
                blockState = blockState.setValue(BlockStateProperties.FACING, Direction.NORTH);
            }
        }

        return blockState;
    }
}
