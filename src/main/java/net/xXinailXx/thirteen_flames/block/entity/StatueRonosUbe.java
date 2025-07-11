package net.xXinailXx.thirteen_flames.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.xXinailXx.thirteen_flames.init.BlockEntityRegistry;
import net.xXinailXx.thirteen_flames.utils.Gods;

public class StatueRonosUbe extends StatueBE {
    public StatueRonosUbe(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.STATUE_RONOS_UNFINISHED.get(), pos, state, Gods.RONOS, false);
    }
}
