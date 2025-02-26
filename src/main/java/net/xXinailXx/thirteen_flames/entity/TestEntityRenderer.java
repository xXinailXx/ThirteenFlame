package net.xXinailXx.thirteen_flames.entity;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import javax.annotation.Nullable;

public class TestEntityRenderer extends GeoEntityRenderer<TestEntity> {
    public TestEntityRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new TestEntityModel());
        this.shadowRadius = 0.3f;
        RenderSystem.disableBlend();
    }

    @Override
    public ResourceLocation getTextureLocation(TestEntity instance) {
        return new ResourceLocation( ThirteenFlames.MODID, "textures/entity/sun_block.png");
    }

    public void render(TestEntity animatable, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render( animatable, entityYaw, partialTick, poseStack, bufferSource, packedLight );
        RenderSystem.disableBlend();
    }

    @Override
    public RenderType getRenderType(TestEntity animatable, float partialTicks, PoseStack poseStack, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        RenderSystem.disableBlend();
        return super.getRenderType(animatable, partialTicks, poseStack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
