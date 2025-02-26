package net.xXinailXx.thirteen_flames.block.custom.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.xXinailXx.thirteen_flames.block.custom.model.StatueRonosModel;
import net.xXinailXx.thirteen_flames.block.entity.StatueRonosBlockEntity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class StatueRonosRenderer extends GeoBlockRenderer<StatueRonosBlockEntity> {
    public StatueRonosRenderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {
        super( rendererDispatcherIn, new StatueRonosModel() );
    }

    @Override
    public RenderType getRenderType(StatueRonosBlockEntity animatable, float partialTick, PoseStack poseStack, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, int packedLight, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }
}