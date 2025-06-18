package net.xXinailXx.thirteen_flames.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.xXinailXx.thirteen_flames.init.BlockEntitiesRegistry;
import net.xXinailXx.thirteen_flames.utils.Gods;

public class StatueRonosBE extends StatueBE<StatueRonosBE> {
    public StatueRonosBE(BlockPos pos, BlockState state) {
        super(BlockEntitiesRegistry.STATUE_RONOS.get(), pos, state, Gods.RONOS, true);
    }
}
