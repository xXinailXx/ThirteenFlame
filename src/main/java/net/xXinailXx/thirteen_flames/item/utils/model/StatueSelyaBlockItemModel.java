package net.xXinailXx.thirteen_flames.item.utils.model;

import net.minecraft.resources.ResourceLocation;
import net.xXinailXx.thirteen_flames.item.StatueSelyaBlockItem;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class StatueSelyaBlockItemModel extends AnimatedGeoModel<StatueSelyaBlockItem> {
    @Override
    public ResourceLocation getModelResource(StatueSelyaBlockItem model) {
        return new ResourceLocation( ThirteenFlames.MODID, "geo/statue_selya_block.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(StatueSelyaBlockItem textures) {
        return new ResourceLocation( ThirteenFlames.MODID, "textures/block/statue_selya_block.png");
    }

    @Override
    public ResourceLocation getAnimationResource(StatueSelyaBlockItem animation) {
        return new ResourceLocation( ThirteenFlames.MODID, "animations/statue_selya_block.animation.json");
    }
}
