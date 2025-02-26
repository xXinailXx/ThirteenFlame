package net.xXinailXx.thirteen_flames.item.utils.model;

import net.minecraft.resources.ResourceLocation;
import net.xXinailXx.thirteen_flames.item.StatueKnefBlockItem;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class StatueKnefBlockItemModel extends AnimatedGeoModel<StatueKnefBlockItem> {
    @Override
    public ResourceLocation getModelResource(StatueKnefBlockItem model) {
        return new ResourceLocation( ThirteenFlames.MODID, "geo/statue_knef_block.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(StatueKnefBlockItem textures) {
        return new ResourceLocation( ThirteenFlames.MODID, "textures/block/statue_knef_block.png");
    }

    @Override
    public ResourceLocation getAnimationResource(StatueKnefBlockItem animation) {
        return new ResourceLocation( ThirteenFlames.MODID, "animations/statue_knef_block.animation.json");
    }
}
