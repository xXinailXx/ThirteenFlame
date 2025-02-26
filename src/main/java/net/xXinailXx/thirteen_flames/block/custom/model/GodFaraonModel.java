package net.xXinailXx.thirteen_flames.block.custom.model;

import net.minecraft.resources.ResourceLocation;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.block.entity.GodFaraonBlockEntity;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class GodFaraonModel extends AnimatedGeoModel<GodFaraonBlockEntity> {
    @Override
    public ResourceLocation getModelResource(GodFaraonBlockEntity model) {
        return new ResourceLocation( ThirteenFlames.MODID, "geo/god_faraon.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GodFaraonBlockEntity textures) {
        return new ResourceLocation( ThirteenFlames.MODID, "textures/block/god_faraon.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GodFaraonBlockEntity animation) {
        return new ResourceLocation( ThirteenFlames.MODID, "animations/god_faraon.animation.json");
    }
}