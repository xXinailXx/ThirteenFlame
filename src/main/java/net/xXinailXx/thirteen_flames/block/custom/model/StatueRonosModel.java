package net.xXinailXx.thirteen_flames.block.custom.model;

import net.minecraft.resources.ResourceLocation;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.block.entity.StatueRonosBE;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class StatueRonosModel extends AnimatedGeoModel<StatueRonosBE> {
    public ResourceLocation getModelResource(StatueRonosBE model) {
        return new ResourceLocation(ThirteenFlames.MODID, "geo/statue_ronos.geo.json");
    }

    public ResourceLocation getTextureResource(StatueRonosBE textures) {
        return new ResourceLocation(ThirteenFlames.MODID, "textures/block/statue_ronos.png");
    }

    public ResourceLocation getAnimationResource(StatueRonosBE animation) {
        return new ResourceLocation(ThirteenFlames.MODID, "animations/statue.animation.json");
    }
}