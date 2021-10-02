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
        Half half = blockState.getValue(BlockStateProperties.HALF);
        Direction direction = blockState.getValue(HorizontalBlock.FACING);

        if (Direction.NORTH.equals(direction)) {
            blockState = blockState.setValue(HorizontalBlock.FACING, Direction.EAST);
        }
        else if (Direction.EAST.equals(direction)) {
            blockState = blockState.setValue(HorizontalBlock.FACING, Direction.SOUTH);
        }
        else if (Direction.SOUTH.equals(direction)) {
            blockState = blockState.setValue(HorizontalBlock.FACING, Direction.WEST);
        }
        else {
            blockState = blockState.setValue(HorizontalBlock.FACING, Direction.NORTH);

            if (Half.BOTTOM.equals(half)) {
                blockState = blockState.setValue(BlockStateProperties.HALF, Half.TOP);
            }
            else {
                blockState = blockState.setValue(BlockStateProperties.HALF, Half.BOTTOM);
            }
        }

        return blockState;
    }
}
