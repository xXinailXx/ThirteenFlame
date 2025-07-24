package net.xXinailXx.thirteen_flames.client.renderer.item;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class EmissiveRenderer extends ItemLevelRenderer {
    public void renderByItem(@NotNull ItemStack pStack, ItemTransforms.@NotNull TransformType pTransformType, @NotNull PoseStack poseStack, @NotNull MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        this.renderLanternOverrides(pStack, pTransformType, poseStack, pBuffer, pPackedLight, pPackedOverlay);
    }

    public void renderLanternOverrides(@NotNull ItemStack stack, @NotNull ItemTransforms.@NotNull TransformType transformType, @NotNull PoseStack pose, @NotNull MultiBufferSource bufferSource, int uv2, int overlay) {
        Minecraft mc = Minecraft.getInstance();
        ItemRenderer ir = mc.getItemRenderer();
        BakedModel isterModel = ir.getModel(stack, mc.level, mc.player, 0);
        ImmutableList<ItemOverrides.BakedOverride> overrides = isterModel.getOverrides().getOverrides();

        for(int i = overrides.size() - 1; i >= 0; --i) {
            ItemOverrides.BakedOverride override = (ItemOverrides.BakedOverride)overrides.get(i);
            int lightmap = uv2;

            if (i == 0)
                lightmap = 15728880;

            this.renderOverrride(override, transformType, pose, stack, bufferSource, (RenderType)null, lightmap, overlay);
        }
    }
}
