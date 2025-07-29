package net.xXinailXx.thirteen_flames.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.xXinailXx.enderdragonlib.client.glow.Beam;
import net.xXinailXx.enderdragonlib.client.glow.GlowData;
import net.xXinailXx.enderdragonlib.utils.RenderUtils;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.client.model.entity.SunSeliasetEntityModel;
import net.xXinailXx.thirteen_flames.entity.SunSeliasetEntity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

import java.util.Random;

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

    public void render(SunSeliasetEntity animatable, float yaw, float partialTick, PoseStack stack, MultiBufferSource bufferSource, int packedLight) {
        if (animatable.getPhase() > 0) {
            stack.pushPose();

            int animTime = animatable.getAnimTime();

            if (animTime == 31)
                animTime = 30;

            stack.translate(0, 0.3 + 0.03 * animTime, 0);
            stack.scale(1F + (0.03F * animTime), 1F + (0.03F * animTime), 1F + (0.03F * animTime));

            GlowData data = animatable.constructGlowData();
            Random random = new Random(1488);

            for (int i = 0; i < data.getBeams().size(); i++) {
                Beam beam = data.getBeams().get(i);

                stack.mulPose(Vector3f.XP.rotationDegrees(random.nextFloat() * 360.0F + animatable.tickCount));
                stack.mulPose(Vector3f.YP.rotationDegrees(random.nextFloat() * 360.0F + animatable.tickCount));
                stack.mulPose(Vector3f.ZP.rotationDegrees(random.nextFloat() * 360.0F + animatable.tickCount));

                RenderUtils.renderBeam(stack, bufferSource, beam.size(), beam.startColor(), beam.endColor());
            }

            stack.popPose();
        }

        super.render(animatable, yaw, partialTick, stack, bufferSource, packedLight);
    }
}
