package net.xXinailXx.thirteen_flames.block.custom.model;

import net.minecraft.resources.ResourceLocation;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.block.entity.StatueSelyaBlockEntity;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class StatueSelyaModel extends AnimatedGeoModel<StatueSelyaBlockEntity> {
    @Override
    public ResourceLocation getModelResource(StatueSelyaBlockEntity model) {
        return new ResourceLocation( ThirteenFlames.MODID, "geo/statue_selya_block.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(StatueSelyaBlockEntity textures) {
        return new ResourceLocation( ThirteenFlames.MODID, "textures/block/statue_selya_block.png");
    }

    @Override
    public ResourceLocation getAnimationResource(StatueSelyaBlockEntity animation) {
        return new ResourceLocation( ThirteenFlames.MODID, "animations/statue_selya_block.animation.json");
    }
}