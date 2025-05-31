package net.xXinailXx.thirteen_flames.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.xXinailXx.thirteen_flames.init.BlockEntityRegistry;
import net.xXinailXx.thirteen_flames.utils.Gods;

public class StatueKnefUBE extends StatueBE<StatueKnefUBE> {
    public StatueKnefUBE(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.STATUE_KNEF_UNFINISHED.get(), pos, state, Gods.KNEF, false);
    }
}
