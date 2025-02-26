package net.xXinailXx.thirteen_flames.block.custom.model;

import net.minecraft.resources.ResourceLocation;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.block.entity.StatueKnefBlockEntity;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class StatueKnefModel extends AnimatedGeoModel<StatueKnefBlockEntity> {
    @Override
    public ResourceLocation getModelResource(StatueKnefBlockEntity model) {
        return new ResourceLocation( ThirteenFlames.MODID, "geo/statue_knef_block.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(StatueKnefBlockEntity textures) {
        return new ResourceLocation( ThirteenFlames.MODID, "textures/block/statue_knef_block.png");
    }

    @Override
    public ResourceLocation getAnimationResource(StatueKnefBlockEntity animation) {
        return new ResourceLocation( ThirteenFlames.MODID, "animations/statue_knef_block.animation.json");
    }
}