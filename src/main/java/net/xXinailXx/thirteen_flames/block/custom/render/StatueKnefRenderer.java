package net.xXinailXx.thirteen_flames.block.custom.render;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.xXinailXx.thirteen_flames.block.custom.model.StatueKnefModel;
import net.xXinailXx.thirteen_flames.block.entity.StatueKnefBE;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class StatueKnefRenderer extends GeoBlockRenderer<StatueKnefBE> {
    public StatueKnefRenderer(BlockEntityRendererProvider.Context context) {
        super(context, new StatueKnefModel());
    }
}