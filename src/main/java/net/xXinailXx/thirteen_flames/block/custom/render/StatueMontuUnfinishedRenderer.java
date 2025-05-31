package net.xXinailXx.thirteen_flames.block.custom.render;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.xXinailXx.thirteen_flames.block.custom.model.StatueMontuUnfinishedModel;
import net.xXinailXx.thirteen_flames.block.entity.StatueMontuUBE;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class StatueMontuUnfinishedRenderer extends GeoBlockRenderer<StatueMontuUBE> {
    public StatueMontuUnfinishedRenderer(BlockEntityRendererProvider.Context context) {
        super(context, new StatueMontuUnfinishedModel());
    }
}