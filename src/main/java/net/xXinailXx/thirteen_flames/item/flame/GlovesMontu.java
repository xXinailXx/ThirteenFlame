package net.xXinailXx.thirteen_flames.item.flame;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import it.hurts.sskirillss.relics.items.relics.base.data.base.RelicData;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityData;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityEntry;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityStat;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicLevelingData;
import it.hurts.sskirillss.relics.items.relics.base.utils.AbilityUtils;
import it.hurts.sskirillss.relics.items.relics.base.utils.LevelingUtils;
import it.hurts.sskirillss.relics.items.relics.base.utils.ResearchUtils;
import it.hurts.sskirillss.relics.utils.DurabilityUtils;
import it.hurts.sskirillss.relics.utils.MathUtils;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.enderdragonlib.api.events.client.EntityInteractEvent;
import net.xXinailXx.enderdragonlib.client.curios.ICurioRenderable;
import net.xXinailXx.enderdragonlib.client.curios.model.CurioArmsModel;
import net.xXinailXx.enderdragonlib.client.curios.model.CurioModel;
import net.xXinailXx.enderdragonlib.client.utils.item.tooltip.ItemBorder;
import net.xXinailXx.thirteen_flames.init.ItemRegistry;
import net.xXinailXx.thirteen_flames.item.base.FlameItemSetting;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.zeith.hammerlib.util.java.tuples.Tuple3;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

import java.util.List;
import java.util.Optional;

@Mod.EventBusSubscriber
public class GlovesMontu extends FlameItemSetting implements ICurioRenderable {
    public RelicData getRelicData() {
        return RelicData.builder().abilityData(RelicAbilityData.builder().ability("fixin", RelicAbilityEntry.builder().maxLevel(10).stat("effective", RelicAbilityStat.builder().initialValue(10, 25).thresholdValue(10, 50).upgradeModifier(RelicAbilityStat.Operation.ADD, 1.5).formatValue((value) -> {
            return MathUtils.round(value, 1);
        }).build()).build()).ability("usin", RelicAbilityEntry.builder().maxLevel(5).stat("boost", RelicAbilityStat.builder().initialValue(5, 15).thresholdValue(5, 50).upgradeModifier(RelicAbilityStat.Operation.ADD, 2).formatValue((value) -> {
            return (int) MathUtils.round(value, 0);
        }).build()).build()).build()).levelingData(new RelicLevelingData(100, 10, 200)).build();
    }

    public CurioModel getModel(ItemStack stack) {
        return new CurioArmsModel(stack.getItem());
    }

    public RenderData getRenderHandData(ItemStack stack, HumanoidArm arm) {
        return switch (arm) {
            case RIGHT -> new RenderData(Vector3f.ZN.rotationDegrees(-5.0F), null, new Tuple3<>(-0.4D, 0.05, 0D));
            case LEFT -> new RenderData(Vector3f.ZN.rotationDegrees(-5.0F), null, new Tuple3<>(0.4D, 0.05, 0D));
        };
    }

    @OnlyIn(Dist.CLIENT)
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        CurioModel model = getModel(stack);

        if (!(model instanceof CurioArmsModel armsModel))
            return;

        matrixStack.pushPose();

        LivingEntity entity = slotContext.entity();

        armsModel.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
        armsModel.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        ICurioRenderer.followBodyRotations(entity, armsModel);

        VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(renderTypeBuffer, RenderType.armorCutoutNoCull(getTexture(stack)), false, stack.hasFoil());

        matrixStack.translate(0, 0, -0.025F);

        armsModel.renderToBuffer(matrixStack, vertexconsumer, light, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);

        matrixStack.popPose();
    }

    public LayerDefinition constructLayerDefinition() {
        MeshDefinition mesh = HumanoidModel.createMesh(new CubeDeformation(0.4F), 0);

        PartDefinition right_arm = mesh.getRoot().addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(0, 0, 0));

        PartDefinition bone_1_r = right_arm.addOrReplaceChild("bone_1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-4, 3, -2.5F, 6, 9, 6, new CubeDeformation(0)).mirror(false)
                 .texOffs(24, 0).addBox(-3, 10, -2, 6, 4, 5, new CubeDeformation(0))
                 .texOffs(0, 21).addBox(-4, 3.5F, -2.5F, 6, 4, 6, new CubeDeformation(0.5F)), PartPose.offset(0, 0, 0));

        PartDefinition bone_2_r = right_arm.addOrReplaceChild("bone_2", CubeListBuilder.create().texOffs(0, 15).mirror().addBox(-1.01F, 7.05F, 0.7F, 3, 4, 2, new CubeDeformation(0)).mirror(false), PartPose.offsetAndRotation(0, 0, 0, -((float)Math.PI / 7.2F), 0, 0));

        PartDefinition left_arm = mesh.getRoot().addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(0, 0, 0));

        PartDefinition bone_1_l = left_arm.addOrReplaceChild("bone_1", CubeListBuilder.create().texOffs(0, 0).addBox(-2, 3, -2.5F, 6, 9, 6, new CubeDeformation(0))
                .texOffs(24, 0).mirror().addBox(-3, 10, -2, 6, 4, 5, new CubeDeformation(0)).mirror(false)
                .texOffs(0, 21).mirror().addBox(-2, 3.5F, -2.5F, 6, 4, 6, new CubeDeformation(0.5F)).mirror(false), PartPose.offset(0, 0, 0));

        PartDefinition bone_2_l = left_arm.addOrReplaceChild("bone_2", CubeListBuilder.create().texOffs(0, 15).addBox(-1.99F, 7.05F, 0.7F, 3, 4, 2, new CubeDeformation(0)), PartPose.offsetAndRotation(0, 0, 0, -((float)Math.PI / 7.2F), 0, 0));

        return LayerDefinition.create(mesh, 48, 48);
    }

    public List<String> bodyParts() {
        return Lists.newArrayList("right_arm", "left_arm");
    }

    public boolean canEquip(String identifier, LivingEntity livingEntity, ItemStack stack) {
        return true;
    }

    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    public ItemBorder constructTooltipData() {
        return ItemBorder.builder()
                .backgroundTop(0x091902)
                .backgroundBottom(0x061001)
                .borderTop(0x1ea400)
                .borderBottom(0x198500)
                .build();
    }

    @SubscribeEvent
    public static void itemEntityInteract(EntityInteractEvent event) {
        Player player = event.getEntity();

        if (!(event.getTarget() instanceof ItemEntity))
            return;

        Optional<ImmutableTriple<String, Integer, ItemStack>> optional = CuriosApi.getCuriosHelper().findEquippedCurio(ItemRegistry.GLOVES_MONTU.get(), player);

        if (optional.isEmpty())
            return;

        ItemStack curio = optional.get().getRight();

        if (curio.isEmpty())
            return;

        if (!ResearchUtils.isItemResearched(player, curio.getItem()))
            return;

        ItemEntity itemEntity = (ItemEntity) event.getEntityHitResult().getEntity();

        if (!player.getLevel().isClientSide)
            return;

        if (player.getMainHandItem().is(ItemRegistry.AURITEH_NUGGET.get())) {
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
