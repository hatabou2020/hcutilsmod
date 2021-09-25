package com.htbcraft.hcutilsmod.mods.direction;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;

public class HorizontalDirectionAdapter implements IBlockDirection {
    private BlockState blockState;

    public HorizontalDirectionAdapter(BlockState blockState) {
        this.blockState = blockState;
    }

    @Override
    public BlockState change() {
        Direction direction = blockState.getValue(HorizontalDirectionalBlock.FACING);

        if (Direction.NORTH.equals(direction)) {
            blockState = blockState.setValue(HorizontalDirectionalBlock.FACING, Direction.EAST);
        }
        else if (Direction.EAST.equals(direction)) {
            blockState = blockState.setValue(HorizontalDirectionalBlock.FACING, Direction.SOUTH);
        }
        else if (Direction.SOUTH.equals(direction)) {
            blockState = blockState.setValue(HorizontalDirectionalBlock.FACING, Direction.WEST);
        }
        else {
            blockState = blockState.setValue(HorizontalDirectionalBlock.FACING, Direction.NORTH);
        }

        return blockState;
    }
}
