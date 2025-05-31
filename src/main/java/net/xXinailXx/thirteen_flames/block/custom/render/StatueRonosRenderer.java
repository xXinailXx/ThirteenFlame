package net.xXinailXx.thirteen_flames.block.custom.render;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.xXinailXx.thirteen_flames.block.custom.model.StatueRonosModel;
import net.xXinailXx.thirteen_flames.block.entity.StatueRonosBE;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class StatueRonosRenderer extends GeoBlockRenderer<StatueRonosBE> {
    public StatueRonosRenderer(BlockEntityRendererProvider.Context context) {
        super(context, new StatueRonosModel());
    }
}