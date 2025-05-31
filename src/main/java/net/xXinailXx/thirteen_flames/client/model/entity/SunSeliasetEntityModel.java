package net.xXinailXx.thirteen_flames.client.model.entity;

import net.minecraft.resources.ResourceLocation;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.entity.SunSeliasetEntity;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SunSeliasetEntityModel extends AnimatedGeoModel<SunSeliasetEntity> {
    public ResourceLocation getModelResource(SunSeliasetEntity object) {
        return new ResourceLocation(ThirteenFlames.MODID, "geo/sun_seliaset.geo.json");
    }

    public ResourceLocation getTextureResource(SunSeliasetEntity object) {
        return new ResourceLocation(ThirteenFlames.MODID, "textures/entity/sun_seliaset.png");
    }

    public ResourceLocation getAnimationResource(SunSeliasetEntity animatable) {
        return new ResourceLocation(ThirteenFlames.MODID, "animations/sun_seliaset.animation.json");
    }
}
