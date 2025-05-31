package net.xXinailXx.thirteen_flames.block.custom.render;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.xXinailXx.thirteen_flames.block.custom.model.StatueRonosUnfinishedModel;
import net.xXinailXx.thirteen_flames.block.entity.StatueRonosUbe;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class StatueRonosUnfinishedRenderer extends GeoBlockRenderer<StatueRonosUbe> {
    public StatueRonosUnfinishedRenderer(BlockEntityRendererProvider.Context context) {
        super(context, new StatueRonosUnfinishedModel());
    }
}