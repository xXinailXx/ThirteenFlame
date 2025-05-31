package net.xXinailXx.thirteen_flames.block.custom.model;

import net.minecraft.resources.ResourceLocation;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.block.entity.StatueSelyaUBE;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class StatueSelyaUnfinishedModel extends AnimatedGeoModel<StatueSelyaUBE> {
    public ResourceLocation getModelResource(StatueSelyaUBE model) {
        return new ResourceLocation(ThirteenFlames.MODID, "geo/statue_selya.geo.json");
    }

    public ResourceLocation getTextureResource(StatueSelyaUBE textures) {
        return new ResourceLocation(ThirteenFlames.MODID, "textures/block/statue_selya_unfinished.png");
    }

    public ResourceLocation getAnimationResource(StatueSelyaUBE animation) {
        return new ResourceLocation(ThirteenFlames.MODID, "animations/statue.animation.json");
    }
}