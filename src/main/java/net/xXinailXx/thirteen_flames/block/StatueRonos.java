package net.xXinailXx.thirteen_flames.block;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.block.custom.model.StatueRonosModel;
import net.xXinailXx.thirteen_flames.init.BlockEntitiesRegistry;
import net.xXinailXx.thirteen_flames.init.BlocksRegistry;
import net.xXinailXx.thirteen_flames.utils.Gods;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class StatueRonos extends StatueHandler {
    public StatueRonos() {
        super(BlocksRegistry.STATUE_RONOS_STRUCTURE.get(), Gods.RONOS, new GeoItemRenderer(new StatueRonosModel()) {
            public ResourceLocation getTextureLocation(Object animatable) {
                return new ResourceLocation(ThirteenFlames.MODID, "textures/block/statue_ronos.png");
            }
        });
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return BlockEntitiesRegistry.STATUE_RONOS.get().create(pos, state);
    }
}
