package com.htbcraft.hcutilsmod.mods.direction;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;

// 階段
public class StairsDirectionAdapter implements IBlockDirection {
    private BlockState blockState;

    public StairsDirectionAdapter(BlockState blockState) {
        this.blockState = blockState;
    }

    @Override
    public BlockState change() {
        Half half = blockState.getValue(BlockStateProperties.HALF);
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
