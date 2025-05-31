package net.xXinailXx.thirteen_flames.block.custom.model;

import net.minecraft.resources.ResourceLocation;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.block.entity.StatueSelyaBE;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class StatueSelyaModel extends AnimatedGeoModel<StatueSelyaBE> {
    public ResourceLocation getModelResource(StatueSelyaBE model) {
        return new ResourceLocation(ThirteenFlames.MODID, "geo/statue_selya.geo.json");
    }

    public ResourceLocation getTextureResource(StatueSelyaBE textures) {
        return new ResourceLocation(ThirteenFlames.MODID, "textures/block/statue_selya.png");
    }

    public ResourceLocation getAnimationResource(StatueSelyaBE animation) {
        return new ResourceLocation(ThirteenFlames.MODID, "animations/statue.animation.json");
    }
}