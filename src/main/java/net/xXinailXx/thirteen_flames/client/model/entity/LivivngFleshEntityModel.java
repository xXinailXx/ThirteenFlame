package net.xXinailXx.thirteen_flames.client.model.entity;

import net.minecraft.resources.ResourceLocation;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.entity.LivingFleshEntity;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class LivivngFleshEntityModel extends AnimatedGeoModel<LivingFleshEntity> {
    public ResourceLocation getModelResource(LivingFleshEntity object) {
        return new ResourceLocation(ThirteenFlames.MODID, "geo/living_flesh.geo.json");
    }

    public ResourceLocation getTextureResource(LivingFleshEntity object) {
        return new ResourceLocation(ThirteenFlames.MODID, "textures/entity/living_flesh.png");
    }

    public ResourceLocation getAnimationResource(LivingFleshEntity animatable) {
        return new ResourceLocation(ThirteenFlames.MODID, "animations/living_flesh.animation.json");
    }
}
