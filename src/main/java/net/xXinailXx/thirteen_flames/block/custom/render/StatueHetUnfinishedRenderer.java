package net.xXinailXx.thirteen_flames.block.custom.render;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.xXinailXx.thirteen_flames.block.custom.model.StatueHetUnfinishedModel;
import net.xXinailXx.thirteen_flames.block.entity.StatueHetUBE;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class StatueHetUnfinishedRenderer extends GeoBlockRenderer<StatueHetUBE> {
    public StatueHetUnfinishedRenderer(BlockEntityRendererProvider.Context context) {
        super(context, new StatueHetUnfinishedModel());
    }
}