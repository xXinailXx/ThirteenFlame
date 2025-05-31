package net.xXinailXx.thirteen_flames.block.custom.model;

import net.minecraft.resources.ResourceLocation;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.block.entity.StatueHetUBE;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class StatueHetUnfinishedModel extends AnimatedGeoModel<StatueHetUBE> {
    public ResourceLocation getModelResource(StatueHetUBE model) {
        return new ResourceLocation(ThirteenFlames.MODID, "geo/statue_het.geo.json");
    }

    public ResourceLocation getTextureResource(StatueHetUBE textures) {
        return new ResourceLocation(ThirteenFlames.MODID, "textures/block/statue_het_unfinished.png");
    }

    public ResourceLocation getAnimationResource(StatueHetUBE animation) {
        return new ResourceLocation(ThirteenFlames.MODID, "animations/statue.animation.json");
    }
}