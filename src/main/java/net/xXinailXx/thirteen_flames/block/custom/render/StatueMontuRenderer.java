package net.xXinailXx.thirteen_flames.block.custom.render;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.xXinailXx.thirteen_flames.block.custom.model.StatueMontuModel;
import net.xXinailXx.thirteen_flames.block.entity.StatueMontuBE;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class StatueMontuRenderer extends GeoBlockRenderer<StatueMontuBE> {
    public StatueMontuRenderer(BlockEntityRendererProvider.Context context) {
        super(context, new StatueMontuModel());
    }
}