package net.xXinailXx.thirteen_flames.block.custom.render;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.xXinailXx.thirteen_flames.block.custom.model.StatueHetModel;
import net.xXinailXx.thirteen_flames.block.entity.StatueHetBE;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class StatueHetRenderer extends GeoBlockRenderer<StatueHetBE> {
    public StatueHetRenderer(BlockEntityRendererProvider.Context context) {
        super(context, new StatueHetModel());
    }
}