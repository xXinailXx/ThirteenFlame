package net.xXinailXx.thirteen_flames.block.custom.model;

import net.minecraft.resources.ResourceLocation;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.block.entity.StatueRonosUbe;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class StatueRonosUnfinishedModel extends AnimatedGeoModel<StatueRonosUbe> {
    public ResourceLocation getModelResource(StatueRonosUbe model) {
        return new ResourceLocation(ThirteenFlames.MODID, "geo/statue_ronos.geo.json");
    }

    public ResourceLocation getTextureResource(StatueRonosUbe textures) {
        return new ResourceLocation(ThirteenFlames.MODID, "textures/block/statue_ronos_unfinished.png");
    }

    public ResourceLocation getAnimationResource(StatueRonosUbe animation) {
        return new ResourceLocation(ThirteenFlames.MODID, "animations/statue.animation.json");
    }
}