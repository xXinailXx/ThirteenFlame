package net.xXinailXx.thirteen_flames.entity;

import net.minecraft.resources.ResourceLocation;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class TestEntityModel extends AnimatedGeoModel<TestEntity> {
    @Override
    public ResourceLocation getModelResource(TestEntity model) {
        return new ResourceLocation( ThirteenFlames.MODID, "geo/sun_block.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(TestEntity textures) {
        return new ResourceLocation( ThirteenFlames.MODID, "textures/entity/sun_block.png");
    }

    @Override
    public ResourceLocation getAnimationResource(TestEntity animation) {
        return new ResourceLocation( ThirteenFlames.MODID, "animations/sun_animation.animation.json");
    }
}
