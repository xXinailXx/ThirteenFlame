package net.xXinailXx.thirteen_flames.block.custom.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.xXinailXx.thirteen_flames.block.custom.model.StatueKnefModel;
import net.xXinailXx.thirteen_flames.block.entity.StatueKnefBlockEntity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class StatueKnefRenderer extends GeoBlockRenderer<StatueKnefBlockEntity> {
    public StatueKnefRenderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {
        super( rendererDispatcherIn, new StatueKnefModel() );
    }

    @Override
    public RenderType getRenderType(StatueKnefBlockEntity animatable, float partialTick, PoseStack poseStack, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, int packedLight, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }
}