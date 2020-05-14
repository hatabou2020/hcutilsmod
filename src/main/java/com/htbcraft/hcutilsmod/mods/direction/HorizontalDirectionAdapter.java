package com.htbcraft.hcutilsmod.mods.direction;

import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.util.Direction;

public class HorizontalDirectionAdapter implements IBlockDirection {
    private BlockState blockState;

    public HorizontalDirectionAdapter(BlockState blockState) {
        this.blockState = blockState;
    }

    @Override
    public BlockState change() {
        Direction direction = blockState.get(HorizontalBlock.HORIZONTAL_FACING);

        if (Direction.NORTH.equals(direction)) {
            blockState = blockState.with(HorizontalBlock.HORIZONTAL_FACING, Direction.EAST);
        }
        else if (Direction.EAST.equals(direction)) {
            blockState = blockState.with(HorizontalBlock.HORIZONTAL_FACING, Direction.SOUTH);
        }
        else if (Direction.SOUTH.equals(direction)) {
            blockState = blockState.with(HorizontalBlock.HORIZONTAL_FACING, Direction.WEST);
        }
        else {
            blockState = blockState.with(HorizontalBlock.HORIZONTAL_FACING, Direction.NORTH);
        }

        return blockState;
    }
}
