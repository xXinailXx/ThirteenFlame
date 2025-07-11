package net.xXinailXx.thirteen_flames.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.xXinailXx.thirteen_flames.init.BlockEntityRegistry;
import net.xXinailXx.thirteen_flames.utils.Gods;

public class StatueSelyaUBE extends StatueBE {
    public StatueSelyaUBE(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.STATUE_SELYA_UNFINISHED.get(), pos, state, Gods.SELYA, false);
    }
}
