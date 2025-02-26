package net.xXinailXx.thirteen_flames.block.custom.model;

import net.minecraft.resources.ResourceLocation;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.block.entity.SunSeliasetBlockEntity;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SunSeliasetModel extends AnimatedGeoModel<SunSeliasetBlockEntity> {
    @Override
    public ResourceLocation getModelResource(SunSeliasetBlockEntity model) {
        return new ResourceLocation( ThirteenFlames.MODID, "geo/sun_block.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SunSeliasetBlockEntity textures) {
        return new ResourceLocation( ThirteenFlames.MODID, "textures/block/sun_block.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SunSeliasetBlockEntity animation) {
        return new ResourceLocation( ThirteenFlames.MODID, "animations/sun_animation.animation.json");
    }
}
