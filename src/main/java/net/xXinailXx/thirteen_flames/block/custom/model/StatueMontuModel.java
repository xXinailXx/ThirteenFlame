package net.xXinailXx.thirteen_flames.block.custom.model;

import net.minecraft.resources.ResourceLocation;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.block.entity.StatueMontuBlockEntity;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class StatueMontuModel extends AnimatedGeoModel<StatueMontuBlockEntity> {
    @Override
    public ResourceLocation getModelResource(StatueMontuBlockEntity model) {
        return new ResourceLocation( ThirteenFlames.MODID, "geo/statue_montu_block.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(StatueMontuBlockEntity textures) {
        return new ResourceLocation( ThirteenFlames.MODID, "textures/block/statue_montu_block.png");
    }

    @Override
    public ResourceLocation getAnimationResource(StatueMontuBlockEntity animation) {
        return new ResourceLocation( ThirteenFlames.MODID, "animations/statue_montu_block.animation.json");
    }
}