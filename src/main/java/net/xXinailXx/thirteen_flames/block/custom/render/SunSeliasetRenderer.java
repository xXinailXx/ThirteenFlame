package net.xXinailXx.thirteen_flames.block.custom.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.xXinailXx.thirteen_flames.block.entity.SunSeliasetBlockEntity;
import net.xXinailXx.thirteen_flames.block.custom.model.SunSeliasetModel;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class SunSeliasetRenderer extends GeoBlockRenderer<SunSeliasetBlockEntity> {
    public SunSeliasetRenderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {
        super( rendererDispatcherIn, new SunSeliasetModel() );
    }

    @Override
    public RenderType getRenderType(SunSeliasetBlockEntity animatable, float partialTick, PoseStack poseStack, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, int packedLight, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }
}
