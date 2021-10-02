package com.htbcraft.hcutilsmod.mods.direction;

import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.util.Direction;

public class ComDirectionAdapter implements IBlockDirection {
    private BlockState blockState;

    public ComDirectionAdapter(BlockState blockState) {
        this.blockState = blockState;
    }

    @Override
    public BlockState change() {
        Direction direction = blockState.getValue(DirectionalBlock.FACING);

        if (Direction.NORTH.equals(direction)) {
            blockState = blockState.setValue(DirectionalBlock.FACING, Direction.EAST);
        }
        else if (Direction.EAST.equals(direction)) {
            blockState = blockState.setValue(DirectionalBlock.FACING, Direction.SOUTH);
        }
        else if (Direction.SOUTH.equals(direction)) {
            blockState = blockState.setValue(DirectionalBlock.FACING, Direction.WEST);
        }
        else if (Direction.WEST.equals((direction))) {
            blockState = blockState.setValue(DirectionalBlock.FACING, Direction.UP);
        }
        else if (Direction.UP.equals((direction))) {
            blockState = blockState.setValue(DirectionalBlock.FACING, Direction.DOWN);
        }
        else {
            blockState = blockState.setValue(DirectionalBlock.FACING, Direction.NORTH);
        }

        return blockState;
    }
}
