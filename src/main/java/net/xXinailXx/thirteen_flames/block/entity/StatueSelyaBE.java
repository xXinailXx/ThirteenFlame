package net.xXinailXx.thirteen_flames.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.xXinailXx.thirteen_flames.init.BlockEntitiesRegistry;
import net.xXinailXx.thirteen_flames.utils.Gods;

public class StatueSelyaBE extends StatueBE<StatueSelyaBE> {
    public StatueSelyaBE(BlockPos pos, BlockState state) {
        super(BlockEntitiesRegistry.STATUE_SELYA.get(), pos, state, Gods.SELYA, true);
    }
}
