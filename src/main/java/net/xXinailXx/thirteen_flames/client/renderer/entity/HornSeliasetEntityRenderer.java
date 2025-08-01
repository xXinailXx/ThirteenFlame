package net.xXinailXx.thirteen_flames.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.xXinailXx.enderdragonlib.client.glow.Beam;
import net.xXinailXx.enderdragonlib.client.glow.GlowData;
import net.xXinailXx.enderdragonlib.utils.RenderUtils;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.client.model.entity.HornSeliasetEntityModel;
import net.xXinailXx.thirteen_flames.entity.HornSeliasetEntity;
import net.xXinailXx.thirteen_flames.entity.SunSeliasetEntity;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

import java.util.Random;

public class HornSeliasetEntityRenderer extends GeoProjectilesRenderer<HornSeliasetEntity> {
    public HornSeliasetEntityRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new HornSeliasetEntityModel());
        this.shadowRadius = 0;
    }

    public ResourceLocation getTextureLocation(HornSeliasetEntity animatable) {
        return new ResourceLocation(ThirteenFlames.MODID, "textures/entity/horn_seliaset.png");
    }

    public void render(HornSeliasetEntity animatable, float yaw, float partialTick, PoseStack stack, MultiBufferSource bufferSource, int packedLight) {
        stack.pushPose();

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
}
