package net.xXinailXx.thirteen_flames.block.custom.model;

import net.minecraft.resources.ResourceLocation;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.block.entity.StatueMontuUBE;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class StatueMontuUnfinishedModel extends AnimatedGeoModel<StatueMontuUBE> {
    public ResourceLocation getModelResource(StatueMontuUBE model) {
        return new ResourceLocation(ThirteenFlames.MODID, "geo/statue_montu.geo.json");
    }

    public ResourceLocation getTextureResource(StatueMontuUBE textures) {
        return new ResourceLocation(ThirteenFlames.MODID, "textures/block/statue_montu_unfinished.png");
    }

    public ResourceLocation getAnimationResource(StatueMontuUBE animation) {
        return new ResourceLocation(ThirteenFlames.MODID, "animations/statue.animation.json");
    }
}