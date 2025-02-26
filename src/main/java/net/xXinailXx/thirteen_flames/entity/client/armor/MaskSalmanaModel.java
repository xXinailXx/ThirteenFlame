package net.xXinailXx.thirteen_flames.entity.client.armor;

import net.minecraft.resources.ResourceLocation;
import net.xXinailXx.thirteen_flames.item.MaskSalmana;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class MaskSalmanaModel extends AnimatedGeoModel<MaskSalmana> {
    @Override
    public ResourceLocation getModelResource(MaskSalmana model) {
        return new ResourceLocation(ThirteenFlames.MODID, "geo/mask_salmana.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MaskSalmana texture) {
        return new ResourceLocation(ThirteenFlames.MODID, "textures/models/armor/mask_salmana.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MaskSalmana animation) {
        return new ResourceLocation(ThirteenFlames.MODID, "animations/mask_salmana.animation.json");
    }
}
