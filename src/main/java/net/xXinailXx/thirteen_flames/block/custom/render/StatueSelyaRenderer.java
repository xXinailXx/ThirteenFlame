package net.xXinailXx.thirteen_flames.block.custom.render;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.xXinailXx.thirteen_flames.block.custom.model.StatueSelyaModel;
import net.xXinailXx.thirteen_flames.block.entity.StatueSelyaBE;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class StatueSelyaRenderer extends GeoBlockRenderer<StatueSelyaBE> {
    public StatueSelyaRenderer(BlockEntityRendererProvider.Context context) {
        super(context, new StatueSelyaModel());
    }
}