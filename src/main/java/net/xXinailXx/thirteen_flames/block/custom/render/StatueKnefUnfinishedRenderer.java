package net.xXinailXx.thirteen_flames.block.custom.render;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.xXinailXx.thirteen_flames.block.custom.model.StatueKnefUnfinishedModel;
import net.xXinailXx.thirteen_flames.block.entity.StatueKnefUBE;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class StatueKnefUnfinishedRenderer extends GeoBlockRenderer<StatueKnefUBE> {
    public StatueKnefUnfinishedRenderer(BlockEntityRendererProvider.Context context) {
        super(context, new StatueKnefUnfinishedModel());
    }
}