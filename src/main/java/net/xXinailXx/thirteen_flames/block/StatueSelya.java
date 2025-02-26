package net.xXinailXx.thirteen_flames.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.xXinailXx.thirteen_flames.init.BlockEntityRegistry;
import net.xXinailXx.thirteen_flames.init.BlockRegistry;
import org.jetbrains.annotations.Nullable;

public class StatueSelya extends StatueHendler {
    @Override
    public void getBlockPlace(Block block) {
        super.getBlockPlace(this);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return BlockEntityRegistry.STATUE_SELYA_BLOCK_ENTITY.get().create(pos, state);
    }

//    @Override
//    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState blockState, boolean drop) {
//        BlockPos mainPos = this.getMainPos(state, pos);
//        getBlocksList(mainPos, true).forEach(blockPos -> level.setBlock(blockPos, BlockRegistry.STATUE_SELYA_STRUCTURE_BLOCK.get().defaultBlockState(), 5));
//        getBlocksList(mainPos, true).forEach(blockPos -> level.destroyBlock(blockPos, false));
//        getBlocksList(mainPos, true).forEach(blockPos -> level.setBlock(blockPos, BlockRegistry.STATUE_SELYA_STRUCTURE_BLOCK.get().defaultBlockState(), 5));
//    }
}
