package net.xXinailXx.thirteen_flames.block.custom.model;

import net.minecraft.resources.ResourceLocation;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.block.entity.StatueRonosBlockEntity;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class StatueRonosModel extends AnimatedGeoModel<StatueRonosBlockEntity> {
    @Override
    public ResourceLocation getModelResource(StatueRonosBlockEntity model) {
        return new ResourceLocation( ThirteenFlames.MODID, "geo/statue_ronos_block.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(StatueRonosBlockEntity textures) {
        return new ResourceLocation( ThirteenFlames.MODID, "textures/block/statue_ronos_block.png");
    }

    @Override
    public ResourceLocation getAnimationResource(StatueRonosBlockEntity animation) {
        return new ResourceLocation( ThirteenFlames.MODID, "animations/statue_ronos_block.animation.json");
    }
}