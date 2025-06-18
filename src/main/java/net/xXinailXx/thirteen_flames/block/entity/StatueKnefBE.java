package net.xXinailXx.thirteen_flames.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.xXinailXx.thirteen_flames.init.BlockEntitiesRegistry;
import net.xXinailXx.thirteen_flames.utils.Gods;

public class StatueKnefBE extends StatueBE<StatueKnefBE> {
    public StatueKnefBE(BlockPos pos, BlockState state) {
        super(BlockEntitiesRegistry.STATUE_KNEF.get(), pos, state, Gods.KNEF, true);
    }
}
