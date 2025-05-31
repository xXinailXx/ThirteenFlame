package net.xXinailXx.thirteen_flames.block.custom.model;

import net.minecraft.resources.ResourceLocation;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.block.entity.StatueHetBE;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class StatueHetModel extends AnimatedGeoModel<StatueHetBE> {
    public ResourceLocation getModelResource(StatueHetBE model) {
        return new ResourceLocation(ThirteenFlames.MODID, "geo/statue_het.geo.json");
    }

    public ResourceLocation getTextureResource(StatueHetBE textures) {
        return new ResourceLocation(ThirteenFlames.MODID, "textures/block/statue_het.png");
    }

    public ResourceLocation getAnimationResource(StatueHetBE animation) {
        return new ResourceLocation(ThirteenFlames.MODID, "animations/statue.animation.json");
    }
}