package net.xXinailXx.thirteen_flames.block.custom.render;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.xXinailXx.thirteen_flames.block.custom.model.StatueGodPharaohUnfinishedModel;
import net.xXinailXx.thirteen_flames.block.entity.StatueGodPharaohUBE;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class StatueGodPharaohUnfinishedRenderer extends GeoBlockRenderer<StatueGodPharaohUBE> {
    public StatueGodPharaohUnfinishedRenderer(BlockEntityRendererProvider.Context context) {
        super(context, new StatueGodPharaohUnfinishedModel());
    }
}