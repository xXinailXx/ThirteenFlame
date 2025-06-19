package net.xXinailXx.thirteen_flames.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.xXinailXx.thirteen_flames.init.BlockEntityRegistry;
import net.xXinailXx.thirteen_flames.utils.Gods;

public class StatueHetBE extends StatueBE<StatueHetBE> {
    public StatueHetBE(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.STATUE_HET.get(), pos, state, Gods.HET, true);
    }
}
