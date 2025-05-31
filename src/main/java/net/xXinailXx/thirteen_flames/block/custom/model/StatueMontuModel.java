package net.xXinailXx.thirteen_flames.block.custom.model;

import net.minecraft.resources.ResourceLocation;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.block.entity.StatueMontuBE;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class StatueMontuModel extends AnimatedGeoModel<StatueMontuBE> {
    public ResourceLocation getModelResource(StatueMontuBE model) {
        return new ResourceLocation(ThirteenFlames.MODID, "geo/statue_montu.geo.json");
    }

    public ResourceLocation getTextureResource(StatueMontuBE textures) {
        return new ResourceLocation(ThirteenFlames.MODID, "textures/block/statue_montu.png");
    }

    public ResourceLocation getAnimationResource(StatueMontuBE animation) {
        return new ResourceLocation(ThirteenFlames.MODID, "animations/statue.animation.json");
    }
}