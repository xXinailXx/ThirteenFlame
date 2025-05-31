package net.xXinailXx.thirteen_flames.block.custom.render;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.xXinailXx.thirteen_flames.block.custom.model.StatueSelyaUnfinishedModel;
import net.xXinailXx.thirteen_flames.block.entity.StatueSelyaUBE;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class StatueSelyaUnfinishedRenderer extends GeoBlockRenderer<StatueSelyaUBE> {
    public StatueSelyaUnfinishedRenderer(BlockEntityRendererProvider.Context context) {
        super(context, new StatueSelyaUnfinishedModel());
    }
}