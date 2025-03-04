package net.xXinailXx.thirteen_flames.client.renderer.item;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.StainedGlassPaneBlock;
import net.xXinailXx.thirteen_flames.mixin.BakedOverrideAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ZeithTechISTER extends BlockEntityWithoutLevelRenderer {
    protected final BlockEntityRenderDispatcher blockEntRenderDispatcher = Minecraft.getInstance().getBlockEntityRenderDispatcher();

    protected ZeithTechISTER() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    protected EntityModelSet getEntityModels() {
        return Minecraft.getInstance().getEntityModels();
    }

    public void renderOverrride(ItemOverrides.BakedOverride override, @NotNull ItemTransforms.@NotNull TransformType transformType, @NotNull PoseStack pose, @NotNull ItemStack stack, @NotNull MultiBufferSource bufferSource, @Nullable RenderType overrideType, int uv2, int overlay) {
        Minecraft mc = Minecraft.getInstance();
        ItemRenderer ir = mc.getItemRenderer();
        BakedModel overridenModel = ((BakedOverrideAccessor)override).getModel();
        if (overridenModel != null) {
            boolean cull;

            label49: {
                if (transformType != ItemTransforms.TransformType.GUI && !transformType.firstPerson()) {
                    Item model = stack.getItem();
                    if (model instanceof BlockItem) {
                        BlockItem bi = (BlockItem)model;
                        Block block = bi.getBlock();
                        cull = !(block instanceof HalfTransparentBlock) && !(block instanceof StainedGlassPaneBlock);
                        break label49;
                    }
                }

                cull = true;
            }

            for(BakedModel model : overridenModel.getRenderPasses(stack, cull)) {
                for(RenderType type : model.getRenderTypes(stack, cull)) {
                    if (overrideType != null)
                        type = overrideType;

                    VertexConsumer vertexconsumer;
                    if (cull)
                        vertexconsumer = ItemRenderer.getFoilBufferDirect(bufferSource, type, true, stack.hasFoil());
                    else
                        vertexconsumer = ItemRenderer.getFoilBuffer(bufferSource, type, true, stack.hasFoil());

                    ir.renderModelLists(overridenModel, stack, uv2, overlay, pose, vertexconsumer);
                }
            }
        }
    }

    public void renderAllOverrides(@NotNull ItemStack stack, @NotNull ItemTransforms.@NotNull TransformType transformType, @NotNull PoseStack pose, @NotNull MultiBufferSource bufferSource, int uv2, int overlay) {
        Minecraft mc = Minecraft.getInstance();
        ItemRenderer ir = mc.getItemRenderer();
        BakedModel isterModel = ir.getModel(stack, mc.level, mc.player, 0);
        ImmutableList<ItemOverrides.BakedOverride> overrides = isterModel.getOverrides().getOverrides();
        UnmodifiableIterator var11 = overrides.iterator();

        while(var11.hasNext()) {
            ItemOverrides.BakedOverride override = (ItemOverrides.BakedOverride)var11.next();
            BakedModel overridenModel = ((BakedOverrideAccessor)override).getModel();
            if (overridenModel != null) {
                boolean cull;

                label43: {
                    if (transformType != ItemTransforms.TransformType.GUI && !transformType.firstPerson()) {
                        Item model = stack.getItem();
                        if (model instanceof BlockItem) {
                            BlockItem bi = (BlockItem)model;
                            Block block = bi.getBlock();
                            cull = !(block instanceof HalfTransparentBlock) && !(block instanceof StainedGlassPaneBlock);
                            break label43;
                        }
                    }

                    cull = true;
                }

                for(BakedModel model : overridenModel.getRenderPasses(stack, cull)) {
                    for(RenderType type : model.getRenderTypes(stack, cull)) {
                        VertexConsumer vertexconsumer;
                        if (cull)
                            vertexconsumer = ItemRenderer.getFoilBufferDirect(bufferSource, type, true, stack.hasFoil());
                        else
                            vertexconsumer = ItemRenderer.getFoilBuffer(bufferSource, type, true, stack.hasFoil());

                        ir.renderModelLists(overridenModel, stack, uv2, overlay, pose, vertexconsumer);
                    }
                }
            }
        }

    }

    public abstract void renderByItem(@NotNull ItemStack var1, @NotNull ItemTransforms.@NotNull TransformType var2, @NotNull PoseStack var3, @NotNull MultiBufferSource var4, int var5, int var6);
}
