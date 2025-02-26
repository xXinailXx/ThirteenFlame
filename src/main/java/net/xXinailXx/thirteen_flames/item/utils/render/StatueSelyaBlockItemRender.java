package net.xXinailXx.thirteen_flames.item.utils.render;

import net.xXinailXx.thirteen_flames.item.StatueSelyaBlockItem;
import net.xXinailXx.thirteen_flames.item.utils.model.StatueSelyaBlockItemModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class StatueSelyaBlockItemRender extends GeoItemRenderer<StatueSelyaBlockItem> {
    public StatueSelyaBlockItemRender() {
        super(new StatueSelyaBlockItemModel());
    }
}
