package net.xXinailXx.thirteen_flames.block.custom.model;

import net.minecraft.resources.ResourceLocation;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.block.entity.StatueGodPharaohUBE;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class StatueGodPharaohUnfinishedModel extends AnimatedGeoModel<StatueGodPharaohUBE> {
    public ResourceLocation getModelResource(StatueGodPharaohUBE model) {
        return new ResourceLocation(ThirteenFlames.MODID, "geo/statue_god_pharaoh.geo.json");
    }

    public ResourceLocation getTextureResource(StatueGodPharaohUBE textures) {
        return new ResourceLocation(ThirteenFlames.MODID, "textures/block/statue_god_pharaoh_unfinished.png");
    }

    public ResourceLocation getAnimationResource(StatueGodPharaohUBE animation) {
        return new ResourceLocation(ThirteenFlames.MODID, "animations/statue.animation.json");
    }
}