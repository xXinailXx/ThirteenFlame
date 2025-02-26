package net.xXinailXx.thirteen_flames.block.custom.model;

import net.minecraft.resources.ResourceLocation;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.block.entity.StatueHetBlockEntity;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class StatueHetModel extends AnimatedGeoModel<StatueHetBlockEntity> {
    @Override
    public ResourceLocation getModelResource(StatueHetBlockEntity model) {
        return new ResourceLocation( ThirteenFlames.MODID, "geo/statue_het_block.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(StatueHetBlockEntity textures) {
        return new ResourceLocation( ThirteenFlames.MODID, "textures/block/statue_het_block.png");
    }

    @Override
    public ResourceLocation getAnimationResource(StatueHetBlockEntity animation) {
        return new ResourceLocation( ThirteenFlames.MODID, "animations/statue_het_block.animation.json");
    }
}