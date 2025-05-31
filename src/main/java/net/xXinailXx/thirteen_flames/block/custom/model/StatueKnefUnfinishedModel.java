package net.xXinailXx.thirteen_flames.block.custom.model;

import net.minecraft.resources.ResourceLocation;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.block.entity.StatueKnefUBE;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class StatueKnefUnfinishedModel extends AnimatedGeoModel<StatueKnefUBE> {
    public ResourceLocation getModelResource(StatueKnefUBE model) {
        return new ResourceLocation(ThirteenFlames.MODID, "geo/statue_knef.geo.json");
    }

    public ResourceLocation getTextureResource(StatueKnefUBE textures) {
        return new ResourceLocation(ThirteenFlames.MODID, "textures/block/statue_knef_unfinished.png");
    }

    public ResourceLocation getAnimationResource(StatueKnefUBE animation) {
        return new ResourceLocation(ThirteenFlames.MODID, "animations/statue.animation.json");
    }
}