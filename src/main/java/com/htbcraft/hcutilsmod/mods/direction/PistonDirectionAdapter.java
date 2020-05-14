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
        if (!blockState.get(BlockStateProperties.EXTENDED)) {
            Direction direction = blockState.get(BlockStateProperties.FACING);

            if (Direction.NORTH.equals(direction)) {
                blockState = blockState.with(BlockStateProperties.FACING, Direction.EAST);
            }
            else if (Direction.EAST.equals(direction)) {
                blockState = blockState.with(BlockStateProperties.FACING, Direction.SOUTH);
            }
            else if (Direction.SOUTH.equals(direction)) {
                blockState = blockState.with(BlockStateProperties.FACING, Direction.WEST);
            }
            else if (Direction.WEST.equals((direction))) {
                blockState = blockState.with(BlockStateProperties.FACING, Direction.UP);
            }
            else if (Direction.UP.equals((direction))) {
                blockState = blockState.with(BlockStateProperties.FACING, Direction.DOWN);
            }
            else {
                blockState = blockState.with(BlockStateProperties.FACING, Direction.NORTH);
            }
        }

        return blockState;
    }
}
