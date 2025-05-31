package net.xXinailXx.thirteen_flames.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.xXinailXx.enderdragonlib.utils.RenderUtils;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.client.model.entity.SunSeliasetEntityModel;
import net.xXinailXx.thirteen_flames.entity.SunSeliasetEntity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class SunSeliasetEntityRenderer extends GeoProjectilesRenderer<SunSeliasetEntity> {
    public SunSeliasetEntityRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SunSeliasetEntityModel());
        this.shadowRadius = 0;
    }

    public ResourceLocation getTextureLocation(SunSeliasetEntity animatable) {
        return new ResourceLocation(ThirteenFlames.MODID, "textures/entity/sun_seliaset.png");
    }

    public RenderType getRenderType(SunSeliasetEntity animatable, float partialTick, PoseStack poseStack, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, int packedLight, ResourceLocation texture) {
        return RenderType.entityTranslucentEmissive(getTextureLocation(animatable));
    }

    public void render(SunSeliasetEntity animatable, float yaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if (animatable.getPhase() > 0) {
            poseStack.pushPose();

            int animTime = animatable.getAnimTime();

            if (animTime == 31)
                animTime = 30;

            poseStack.translate(0, 0.015 * animTime, 0);
            poseStack.scale(1F + (0.03F * animTime), 1F + (0.03F * animTime), 1F + (0.03F * animTime));

            RenderUtils.renderBeams(poseStack, bufferSource, animatable.tickCount, animatable.constructGlowData());

            poseStack.popPose();
        }

        super.render(animatable, yaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
