package net.xXinailXx.thirteen_flames.client.renderer.item;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.init.ItemRegistry;
import org.jetbrains.annotations.NotNull;

public class MoonBowRenderer extends ItemLevelRenderer {
    public void renderByItem(ItemStack pStack, ItemTransforms.TransformType pTransformType, PoseStack poseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        this.renderLanternOverrides(pStack, pTransformType, poseStack, pBuffer, pPackedLight, pPackedOverlay);
    }

    public void renderLanternOverrides(@NotNull ItemStack stack, @NotNull ItemTransforms.@NotNull TransformType transformType, @NotNull PoseStack pose, @NotNull MultiBufferSource bufferSource, int uv2, int overlay) {
        Minecraft mc = Minecraft.getInstance();
        ItemRenderer ir = mc.getItemRenderer();
        BakedModel isterModel = ir.getModel(stack, mc.level, mc.player, 0);
        ImmutableList<ItemOverrides.BakedOverride> overrides = isterModel.getOverrides().getOverrides();

        for(int i = overrides.size() - 1; i >= 0; --i) {
            ItemOverrides.BakedOverride override = overrides.get(i);
            int lightmap = 16711935;
            float pull = ItemProperties.getProperty(ItemRegistry.MOON_BOW.get(), new ResourceLocation(ThirteenFlames.MODID, "pull")).call(stack, mc.level, mc.player, 0);

            if ((double)pull < 0.1) {
                this.renderOverrride(overrides.get(7), transformType, pose, stack, bufferSource, null, uv2, overlay);
                this.renderOverrride(overrides.get(6), transformType, pose, stack, bufferSource, null, lightmap, overlay);
            } else if ((double)pull < 0.65) {
                this.renderOverrride(overrides.get(5), transformType, pose, stack, bufferSource, null, uv2, overlay);
                this.renderOverrride(overrides.get(4), transformType, pose, stack, bufferSource, null, lightmap, overlay);
            } else if ((double)pull < 0.9) {
                this.renderOverrride(overrides.get(3), transformType, pose, stack, bufferSource, null, uv2, overlay);
                this.renderOverrride(overrides.get(2), transformType, pose, stack, bufferSource, null, lightmap, overlay);
            } else {
                this.renderOverrride(overrides.get(1), transformType, pose, stack, bufferSource, null, uv2, overlay);
                this.renderOverrride(overrides.get(0), transformType, pose, stack, bufferSource, null, lightmap, overlay);
            }
        }
    }
}
