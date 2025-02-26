package net.xXinailXx.thirteen_flames.item.utils.render;

import net.xXinailXx.thirteen_flames.item.StatueKnefBlockItem;
import net.xXinailXx.thirteen_flames.item.utils.model.StatueKnefBlockItemModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class StatueKnefBlockItemRender extends GeoItemRenderer<StatueKnefBlockItem> {
    public StatueKnefBlockItemRender() {
        super(new StatueKnefBlockItemModel());
    }
}
