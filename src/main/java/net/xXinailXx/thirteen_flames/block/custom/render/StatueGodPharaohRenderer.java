package net.xXinailXx.thirteen_flames.block.custom.render;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.xXinailXx.thirteen_flames.block.custom.model.StatueGodPharaohModel;
import net.xXinailXx.thirteen_flames.block.entity.StatueGodPharaohBE;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class StatueGodPharaohRenderer extends GeoBlockRenderer<StatueGodPharaohBE> {
    public StatueGodPharaohRenderer(BlockEntityRendererProvider.Context context) {
        super(context, new StatueGodPharaohModel());
    }
}