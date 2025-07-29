package net.xXinailXx.thirteen_flames.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.xXinailXx.thirteen_flames.init.BlockEntityRegistry;
import net.xXinailXx.thirteen_flames.init.BlockRegistry;
import net.xXinailXx.thirteen_flames.utils.Gods;
import org.jetbrains.annotations.Nullable;

public class StatueSelyaUnfinished extends StatueHandler {
    public StatueSelyaUnfinished() {
        super(BlockRegistry.STATUE_SELYA_STRUCTURE.get(), Gods.SELYA);
    }

    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return BlockEntityRegistry.STATUE_SELYA_UNFINISHED.get().create(pos, state);
    }
}
