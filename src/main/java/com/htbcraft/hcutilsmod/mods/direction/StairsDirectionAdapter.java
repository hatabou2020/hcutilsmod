package com.htbcraft.hcutilsmod.mods.direction;

import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.Half;
import net.minecraft.util.Direction;

// 階段
public class StairsDirectionAdapter implements IBlockDirection {
    private BlockState blockState;

    public StairsDirectionAdapter(BlockState blockState) {
        this.blockState = blockState;
    }

    @Override
    public BlockState change() {
        Half half = blockState.get(BlockStateProperties.HALF);
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

            if (Half.BOTTOM.equals(half)) {
                blockState = blockState.with(BlockStateProperties.HALF, Half.TOP);
            }
            else {
                blockState = blockState.with(BlockStateProperties.HALF, Half.BOTTOM);
            }
        }

        return blockState;
    }
}
