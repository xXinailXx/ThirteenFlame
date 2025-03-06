package net.xXinailXx.thirteen_flames.item.flame;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.hurts.sskirillss.relics.client.tooltip.base.RelicStyleData;
import it.hurts.sskirillss.relics.items.relics.base.data.base.RelicData;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityData;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityEntry;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityStat;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicLevelingData;
import it.hurts.sskirillss.relics.items.relics.base.utils.AbilityUtils;
import it.hurts.sskirillss.relics.items.relics.base.utils.LevelingUtils;
import it.hurts.sskirillss.relics.utils.DurabilityUtils;
import it.hurts.sskirillss.relics.utils.MathUtils;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.enderdragonlib.api.events.client.ItemEntityInteractEvent;
import net.xXinailXx.enderdragonlib.client.curios.ICuriosRenderable;
import net.xXinailXx.enderdragonlib.client.curios.model.CuriosModel;
import net.xXinailXx.enderdragonlib.client.curios.model.CuriosHandsSidedModel;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.init.ItemsRegistry;
import net.xXinailXx.thirteen_flames.utils.FlameItemSetting;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

import java.util.List;
import java.util.Optional;

public class GlovesMontu extends FlameItemSetting {
    public RelicData getRelicData() {
        return RelicData.builder().abilityData(RelicAbilityData.builder().ability("fixin", RelicAbilityEntry.builder().maxLevel(10).stat("effective", RelicAbilityStat.builder().initialValue(10.0, 25.0).upgradeModifier(RelicAbilityStat.Operation.ADD, 1.5).formatValue((value) -> {
            return MathUtils.round(value, 1);
        }).build()).build()).ability("usin", RelicAbilityEntry.builder().requiredLevel(0).maxLevel(5).stat("boost", RelicAbilityStat.builder().initialValue(5.0, 15.0).upgradeModifier(RelicAbilityStat.Operation.ADD, 1.0).formatValue((value) -> {
            return MathUtils.round(value, 1);
        }).build()).build()).build()).levelingData(new RelicLevelingData(100, 10, 100)).styleData(RelicStyleData.builder().build()).build();
    }

//    @Override
//    public CuriosModel getModel(ItemStack stack) {
//        return new CuriosHandsSidedModel(stack.getItem());
//    }
//
//    @OnlyIn(Dist.CLIENT)
//    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
//        CuriosModel model = getModel(stack);
//
//        if (!(model instanceof CuriosHandsSidedModel sidedModel))
//            return;
//
//        sidedModel.setSlot(slotContext.index());
//
//        matrixStack.pushPose();
//
//        LivingEntity entity = slotContext.entity();
//
//        sidedModel.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
//        sidedModel.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
//
//        ICurioRenderer.followBodyRotations(entity, sidedModel);
//
//        VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(renderTypeBuffer, RenderType.armorCutoutNoCull(getTexture(stack)), false, stack.hasFoil());
//
//        matrixStack.translate(0, 0, -0.025F);
//
//        sidedModel.renderToBuffer(matrixStack, vertexconsumer, light, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
//
//        matrixStack.popPose();
//    }
//
//    public LayerDefinition constructLayerDefinition() {
//        MeshDefinition mesh = HumanoidModel.createMesh(new CubeDeformation(0.4F), 0);
//
//        mesh.getRoot().addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-4.0F, 6.0F, -2.5F, 3.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false)
//                .texOffs(0, 13).mirror().addBox(-1.0F, 6.0F, -2.5F, 3.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false)
//                .texOffs(0, 0).mirror().addBox(-4.5F, 8.0F, -0.5F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.0F, 2.0F, -0.5F));
//
//        mesh.getRoot().addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(0, 0).addBox(1.0F, 6.0F, -2.5F, 3.0F, 7.0F, 6.0F, new CubeDeformation(0.0F))
//                .texOffs(0, 13).addBox(-2.0F, 6.0F, -2.5F, 3.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
//                .texOffs(0, 0).addBox(3.5F, 8.0F, -0.5F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 2.0F, -0.5F));
//
//        return LayerDefinition.create(mesh, 32, 32);
//    }
//
//    public List<String> bodyParts() {
//        return Lists.newArrayList("right_arm", "left_arm");
//    }

    public boolean canEquip(String identifier, LivingEntity livingEntity, ItemStack stack) {
        return true;
    }

    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    @Mod.EventBusSubscriber
    public static class Event {
        @SubscribeEvent
        public static void itemEntityInteract(ItemEntityInteractEvent event) {
            Player player = event.getEntity();

            Optional<ImmutableTriple<String, Integer, ItemStack>> optional = CuriosApi.getCuriosHelper().findEquippedCurio(ItemsRegistry.GLOVES_MONTU.get(), player);

            if (optional.isEmpty())
                return;

            ItemStack curio = optional.get().getRight();

            if (curio.isEmpty())
                return;

            ItemEntity itemEntity = (ItemEntity) event.getEntityHitResult().getEntity();

            if (itemEntity == null && !player.getLevel().isClientSide)
                return;

            if (player.getMainHandItem().is(ItemsRegistry.AURITEH_NUGGET.get())) {
                ItemStack stack = itemEntity.getItem().copy();

                if (stack.getDamageValue() < stack.getMaxDamage()) {
                    double value = AbilityUtils.getAbilityValue(curio, "fixin", "effective");

                    DurabilityUtils.repair(stack, (int) (stack.getMaxDamage() * ((int) value * 0.01)));

                    itemEntity.setItem(stack);

                    if (!player.isCreative())
                        player.getMainHandItem().shrink(1);

                    LevelingUtils.addExperience(player, curio, 5);
                }
            }
        }
    }
}
