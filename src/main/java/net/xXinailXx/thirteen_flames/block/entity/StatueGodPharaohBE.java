package net.xXinailXx.thirteen_flames.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.xXinailXx.thirteen_flames.init.BlockEntityRegistry;
import net.xXinailXx.thirteen_flames.utils.Gods;

public class StatueGodPharaohBE extends StatueBE<StatueGodPharaohBE> {
    public StatueGodPharaohBE(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.STATUE_GOD_PHARAOH.get(), pos, state, Gods.GOD_PHARAOH, true);
    }
}
