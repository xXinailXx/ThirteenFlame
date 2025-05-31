package net.xXinailXx.thirteen_flames.block.custom.model;

import net.minecraft.resources.ResourceLocation;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.block.entity.StatueGodPharaohBE;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class StatueGodPharaohModel extends AnimatedGeoModel<StatueGodPharaohBE> {
    public ResourceLocation getModelResource(StatueGodPharaohBE model) {
        return new ResourceLocation(ThirteenFlames.MODID, "geo/statue_god_pharaoh.geo.json");
    }

    public ResourceLocation getTextureResource(StatueGodPharaohBE textures) {
        return new ResourceLocation(ThirteenFlames.MODID, "textures/block/statue_god_pharaoh.png");
    }

    public ResourceLocation getAnimationResource(StatueGodPharaohBE animation) {
        return new ResourceLocation(ThirteenFlames.MODID, "animations/statue.animation.json");
    }
}