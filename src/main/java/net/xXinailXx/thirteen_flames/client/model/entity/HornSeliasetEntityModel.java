package net.xXinailXx.thirteen_flames.client.model.entity;

import net.minecraft.resources.ResourceLocation;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.entity.HornSeliasetEntity;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class HornSeliasetEntityModel extends AnimatedGeoModel<HornSeliasetEntity> {
    public ResourceLocation getModelResource(HornSeliasetEntity object) {
        return new ResourceLocation(ThirteenFlames.MODID, "geo/horn_seliaset.geo.json");
    }

    public ResourceLocation getTextureResource(HornSeliasetEntity object) {
        return new ResourceLocation(ThirteenFlames.MODID, "textures/entity/horn_seliaset.png");
    }

    public ResourceLocation getAnimationResource(HornSeliasetEntity animatable) {
        return new ResourceLocation(ThirteenFlames.MODID, "animations/horn_seliaset.animation.json");
    }
}
