package com.htbcraft.hcutilsmod.mods.direction;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SlabType;

public class SlabDirectionAdapter implements IBlockDirection {
    private BlockState blockState;

    public SlabDirectionAdapter(BlockState blockState) {
        this.blockState = blockState;
    }

    @Override
    public BlockState change() {
        SlabType slabType = blockState.getValue(BlockStateProperties.SLAB_TYPE);

        if (SlabType.BOTTOM.equals(slabType)) {
            blockState = blockState.setValue(BlockStateProperties.SLAB_TYPE, SlabType.TOP);
        }
        else {
            blockState = blockState.setValue(BlockStateProperties.SLAB_TYPE, SlabType.BOTTOM);
        }

        return blockState;
    }
}
