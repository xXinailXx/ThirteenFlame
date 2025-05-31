package net.xXinailXx.thirteen_flames.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.client.model.entity.HornSeliasetEntityModel;
import net.xXinailXx.thirteen_flames.entity.HornSeliasetEntity;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class HornSeliasetEntityRenderer extends GeoProjectilesRenderer<HornSeliasetEntity> {
    public HornSeliasetEntityRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new HornSeliasetEntityModel());
        this.shadowRadius = 0;
    }

    public ResourceLocation getTextureLocation(HornSeliasetEntity animatable) {
        return new ResourceLocation(ThirteenFlames.MODID, "textures/entity/horn_seliaset.png");
    }
}
