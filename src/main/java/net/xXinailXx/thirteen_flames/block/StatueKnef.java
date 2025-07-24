package net.xXinailXx.thirteen_flames.block;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.block.custom.model.StatueKnefModel;
import net.xXinailXx.thirteen_flames.init.BlockEntityRegistry;

import net.xXinailXx.thirteen_flames.init.BlockRegistry;
import net.xXinailXx.thirteen_flames.utils.Gods;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class StatueKnef extends StatueHandler {
    public StatueKnef() {
        super(BlockRegistry.STATUE_KNEF_STRUCTURE.get(), Gods.KNEF);
    }

    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return BlockEntityRegistry.STATUE_KNEF.get().create(pos, state);
    }
}
