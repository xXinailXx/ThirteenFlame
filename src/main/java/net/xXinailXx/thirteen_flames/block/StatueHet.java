package net.xXinailXx.thirteen_flames.block;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.block.custom.model.StatueHetModel;
import net.xXinailXx.thirteen_flames.init.BlockEntitiesRegistry;
import net.xXinailXx.thirteen_flames.init.BlocksRegistry;
import net.xXinailXx.thirteen_flames.utils.Gods;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class StatueHet extends StatueHandler {
    public StatueHet() {
        super(BlocksRegistry.STATUE_HET_STRUCTURE.get(), Gods.HET, new GeoItemRenderer(new StatueHetModel()) {
            public ResourceLocation getTextureLocation(Object animatable) {
                return new ResourceLocation(ThirteenFlames.MODID, "textures/block/statue_het.png");
            }
        });
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return BlockEntitiesRegistry.STATUE_HET.get().create(pos, state);
    }
}
