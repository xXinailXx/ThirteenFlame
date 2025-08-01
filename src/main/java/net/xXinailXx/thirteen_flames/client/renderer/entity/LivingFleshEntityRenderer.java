package net.xXinailXx.thirteen_flames.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.entity.LivingFleshEntity;
import net.xXinailXx.thirteen_flames.client.model.entity.LivivngFleshEntityModel;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class LivingFleshEntityRenderer extends GeoEntityRenderer<LivingFleshEntity> {
    public LivingFleshEntityRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new LivivngFleshEntityModel());
        this.shadowRadius = 0;
    }

    public ResourceLocation getTextureLocation(LivingFleshEntity animatable) {
        return new ResourceLocation(ThirteenFlames.MODID, "textures/entity/living_flesh.png");
    }

    public RenderType getRenderType(LivingFleshEntity animatable, float partialTick, PoseStack poseStack, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, int packedLight, ResourceLocation texture) {
        poseStack.scale(0.25F + animatable.getSize() * 0.01F, 0.25F + animatable.getSize() * 0.01F, 0.25F + animatable.getSize() * 0.01F);

        return super.getRenderType(animatable, partialTick, poseStack, bufferSource, buffer, packedLight, texture);
    }
}
