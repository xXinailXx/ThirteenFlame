package net.xXinailXx.thirteen_flames.block;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.block.custom.model.StatueKnefModel;
import net.xXinailXx.thirteen_flames.init.BlockEntitiesRegistry;

import net.xXinailXx.thirteen_flames.init.BlocksRegistry;
import net.xXinailXx.thirteen_flames.utils.Gods;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class StatueKnef extends StatueHandler {
    public StatueKnef() {
        super(BlocksRegistry.STATUE_KNEF_STRUCTURE.get(), Gods.KNEF, new GeoItemRenderer(new StatueKnefModel()) {
            public ResourceLocation getTextureLocation(Object animatable) {
                return new ResourceLocation(ThirteenFlames.MODID, "textures/block/statue_knef.png");
            }
        });
    }

    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return BlockEntitiesRegistry.STATUE_KNEF.get().create(pos, state);
    }
}
