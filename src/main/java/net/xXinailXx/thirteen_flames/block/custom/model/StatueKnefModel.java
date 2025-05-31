package net.xXinailXx.thirteen_flames.block.custom.model;

import net.minecraft.resources.ResourceLocation;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.block.entity.StatueKnefBE;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class StatueKnefModel extends AnimatedGeoModel<StatueKnefBE> {
    public ResourceLocation getModelResource(StatueKnefBE model) {
        return new ResourceLocation(ThirteenFlames.MODID, "geo/statue_knef.geo.json");
    }

    public ResourceLocation getTextureResource(StatueKnefBE textures) {
        return new ResourceLocation(ThirteenFlames.MODID, "textures/block/statue_knef.png");
    }

    public ResourceLocation getAnimationResource(StatueKnefBE animation) {
        return new ResourceLocation(ThirteenFlames.MODID, "animations/statue.animation.json");
    }
}