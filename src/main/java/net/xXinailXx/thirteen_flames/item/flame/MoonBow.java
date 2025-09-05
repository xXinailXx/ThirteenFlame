package net.xXinailXx.thirteen_flames.item.flame;

import com.google.common.base.Suppliers;
import it.hurts.sskirillss.relics.client.particles.circle.CircleTintData;
import it.hurts.sskirillss.relics.items.relics.base.data.base.RelicData;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityData;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityEntry;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityStat;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicLevelingData;
import it.hurts.sskirillss.relics.items.relics.base.utils.AbilityUtils;
import it.hurts.sskirillss.relics.items.relics.base.utils.ResearchUtils;
import it.hurts.sskirillss.relics.utils.MathUtils;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.enderdragonlib.client.utils.item.tooltip.ItemBorder;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.client.renderer.item.MoonBowRenderer;
import net.xXinailXx.thirteen_flames.entity.MoonCarrierEntity;
import net.xXinailXx.thirteen_flames.entity.MoonProjectileEntity;
import net.xXinailXx.thirteen_flames.entity.MoonProjectileSpecialEntity;
import net.xXinailXx.thirteen_flames.entity.MoonStormcallerEntity;
import net.xXinailXx.thirteen_flames.init.EntityRegistry;
import net.xXinailXx.thirteen_flames.init.SoundRegistry;
import net.xXinailXx.thirteen_flames.item.base.FlameItemSetting;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Mod.EventBusSubscriber
public class MoonBow extends FlameItemSetting {
    public static final DamageSource SUCC = (new DamageSource(ThirteenFlames.MODID + ":succ")).bypassArmor().bypassEnchantments().bypassMagic().bypassInvul();
    private final Random random = new Random();
    private boolean isSurging;

    public RelicData getRelicData() {
        return RelicData.builder().abilityData(RelicAbilityData.builder().ability("shot", RelicAbilityEntry.builder().maxLevel(10).stat("rays", RelicAbilityStat.builder().initialValue(3, 3).thresholdValue(3, 23).upgradeModifier(RelicAbilityStat.Operation.ADD, 2).formatValue((value) -> {
            return (int) MathUtils.round(value, 1);
        }).build()).stat("damage", RelicAbilityStat.builder().initialValue(3, 4).thresholdValue(3, 5).upgradeModifier(RelicAbilityStat.Operation.ADD, 0.1).formatValue((value) -> {
            return MathUtils.round(value, 1);
        }).build()).stat("drain", RelicAbilityStat.builder().initialValue(0.25, 0.2).thresholdValue(0.05, 0.25).upgradeModifier(RelicAbilityStat.Operation.ADD, -0.015).formatValue((value) -> {
            return MathUtils.round(value * 100, 1);
        }).build()).build()).ability("swim", RelicAbilityEntry.builder().maxLevel(5).stat("speed", RelicAbilityStat.builder().initialValue(4, 6).thresholdValue(4, 11).upgradeModifier(RelicAbilityStat.Operation.ADD, 1).formatValue((value) -> {
            return MathUtils.round(value, 1);
        }).build()).stat("damage", RelicAbilityStat.builder().initialValue(6, 8).thresholdValue(6, 20).upgradeModifier(RelicAbilityStat.Operation.ADD, 2.4).formatValue((value) -> {
            return MathUtils.round(value, 1);
        }).build()).build()).ability("storm", RelicAbilityEntry.builder().requiredLevel(10).maxLevel(5).stat("radius", RelicAbilityStat.builder().initialValue(4, 5).thresholdValue(4, 20).upgradeModifier(RelicAbilityStat.Operation.ADD, 3).formatValue((value) -> {
            return MathUtils.round(value, 1);
        }).build()).stat("duration", RelicAbilityStat.builder().initialValue(11, 16).thresholdValue(11, 32).upgradeModifier(RelicAbilityStat.Operation.ADD, 4).formatValue((value) -> {
            return (int)MathUtils.round(value, 1);
        }).build()).stat("damage", RelicAbilityStat.builder().initialValue(6, 8).thresholdValue(6, 13).upgradeModifier(RelicAbilityStat.Operation.ADD, 1).formatValue((value) -> {
            return (int)MathUtils.round(value, 1);
        }).build()).stat("heal", RelicAbilityStat.builder().initialValue(2, 3).thresholdValue(2, 10).upgradeModifier(RelicAbilityStat.Operation.ADD, 1.4).formatValue((value) -> {
            return MathUtils.round(value, 1);
        }).build()).build()).build()).levelingData(new RelicLevelingData(100, 20, 100)).build();
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        player.startUsingItem(hand);
        return InteractionResultHolder.consume(itemstack);
    }

    public void releaseUsing(@NotNull ItemStack stack, @NotNull Level level, LivingEntity entity, int timeCharged) {
        if (!entity.isEyeInFluidType(ForgeMod.WATER_TYPE.get()) && entity instanceof Player player) {
            if (player.isCrouching() && AbilityUtils.canUseAbility(stack, "storm") && !AbilityUtils.isAbilityOnCooldown(stack, "storm")) {
                if (this.getUseDuration(stack) - timeCharged > 19 && !AbilityUtils.isAbilityOnCooldown(stack, "storm")) {
                    level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundRegistry.MOON_BOW_SHOT.get(), SoundSource.MASTER, 0.7F, 0.6F);
                    level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundRegistry.MOON_BOW_SHOT.get(), SoundSource.MASTER, 0.6F, 0.3F);

                    MoonStormcallerEntity stormcaller = new MoonStormcallerEntity(EntityRegistry.MOON_STORMCALLER.get(), level);
                    Vec3 pos = entity.getEyePosition(1.0F).add(entity.getLookAngle().scale(0.3)).add(entity.getLookAngle().cross(entity.getLookAngle().x < 0.001 && entity.getLookAngle().z < 0.001 ? Vec3.directionFromRotation(0.0F, entity.getYHeadRot()).scale(entity.getLookAngle().y > 0 ? -1 : 1).normalize() : new Vec3(0, 1, 0)).normalize().scale(0.2)).add(0, -0.13, 0).subtract(entity.getLookAngle().scale(1.4));
                    stormcaller.setPos(pos);
                    stormcaller.setOwner(entity);
                    stormcaller.shotPos = pos;
                    stormcaller.setBow(stack);
                    stormcaller.setRays(MoonProjectileSpecialEntity.makeList(6, level, entity, pos, entity.getLookAngle().scale(0.3)));
                    stormcaller.shootFromRotation(entity, entity.getXRot(), entity.getYRot(), 0.75F, 2.5F, 0.0F);

                    for(MoonProjectileSpecialEntity proj : stormcaller.rays)
                        level.addFreshEntity(proj);

                    level.addFreshEntity(stormcaller);
                    AbilityUtils.addAbilityCooldown(stack, "storm", 600);
                } else if (this.getUseDuration(stack) - timeCharged > 5) {
                    if (!level.isClientSide()) {
                        float fl = this.random.nextFloat();
                        level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundRegistry.MOON_BOW_SHOT.get(), SoundSource.MASTER, 0.5F, 1.75F + fl * 0.1F);
                    }

                    Vec3 pos = entity.getEyePosition(1.0F).add(entity.getLookAngle().scale(0.3)).add(entity.getLookAngle().cross(entity.getLookAngle().x < 0.001 && entity.getLookAngle().z < 0.001 ? Vec3.directionFromRotation(0.0F, entity.getYHeadRot()).scale(entity.getLookAngle().y > 0 ? -1 : 1).normalize() : new Vec3(0, 1, 0)).normalize().scale(0.2)).add(0, -0.13, 0).subtract(entity.getLookAngle().scale(1.4));
                    MoonProjectileEntity proj = new MoonProjectileEntity(EntityRegistry.MOON_PROJECTILE.get(), level);
                    proj.setPos(pos);
                    proj.setOwner(entity);
                    proj.setPowerEnch(stack.getEnchantmentLevel(Enchantments.POWER_ARROWS));
                    proj.setBow(stack);
                    proj.setFree(false);
                    proj.shootFromRotation(entity, entity.getXRot(), entity.getYRot(), 0.75F, 1.0F, 0.0F);
                    level.addFreshEntity(proj);
                }
            } else if (this.getUseDuration(stack) - timeCharged > 19) {
                if (!level.isClientSide()) {
                    float fl = this.random.nextFloat();
                    level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundRegistry.MOON_BOW_SHOT.get(), SoundSource.MASTER, 0.5F, 1.8F - fl * 0.15F);
                    level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundRegistry.MOON_BOW_SHOT.get(), SoundSource.MASTER, 0.5F, 0.6F);
                }

                int count = (int)AbilityUtils.getAbilityValue(stack, "shot", "rays");
                Vec3 pos = entity.getEyePosition(1.0F).add(entity.getLookAngle().scale(0.3)).add(entity.getLookAngle().cross(entity.getLookAngle().x < 0.001 && entity.getLookAngle().z < 0.001 ? Vec3.directionFromRotation(0.0F, entity.getYHeadRot()).scale(entity.getLookAngle().y > 0 ? -1 : 1).normalize() : new Vec3(0, 1, 0)).normalize().scale(0.2)).add(0, -0.13, 0).subtract(entity.getLookAngle().scale(1.4));
                MoonCarrierEntity carrier = (new MoonCarrierEntity(EntityRegistry.MOON_CARRIES.get(), level)).setRays(MoonProjectileEntity.makeList(count, level, entity, pos, entity.getLookAngle().scale(0.3), stack.getEnchantmentLevel(Enchantments.POWER_ARROWS), stack));
                carrier.setPos(pos);
                carrier.setOwner(entity);
                carrier.shootFromRotation(entity, entity.getXRot(), entity.getYRot(), 0.75F, 1.0F, 0.0F);
                level.addFreshEntity(carrier);

                for(MoonProjectileEntity proj : carrier.rays)
                    level.addFreshEntity(proj);
            } else if (this.getUseDuration(stack) - timeCharged > 5) {
                if (!level.isClientSide()) {
                    float fl = this.random.nextFloat();
                    level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundRegistry.MOON_BOW_SHOT.get(), SoundSource.MASTER, 0.5F, 1.75F + fl * 0.1F);
                }

                Vec3 pos = entity.getEyePosition(1.0F).add(entity.getLookAngle().scale(0.3)).add(entity.getLookAngle().cross(entity.getLookAngle().x < 0.001 && entity.getLookAngle().z < 0.001 ? Vec3.directionFromRotation(0.0F, entity.getYHeadRot()).scale(entity.getLookAngle().y > 0 ? -1 : 1).normalize() : new Vec3(0, 1, 0)).normalize().scale(0.2)).add(0, -0.13, 0).subtract(entity.getLookAngle().scale(1.4));
                MoonProjectileEntity proj = new MoonProjectileEntity(EntityRegistry.MOON_PROJECTILE.get(), level);
                proj.setPos(pos);
                proj.setOwner(entity);
                proj.setPowerEnch(stack.getEnchantmentLevel(Enchantments.POWER_ARROWS));
                proj.setBow(stack);
                proj.shootFromRotation(entity, entity.getXRot(), entity.getYRot(), 0.75F, 1.0F, 0.0F);
                level.addFreshEntity(proj);
            }
        }

        this.isSurging = false;
    }

    public void onUsingTick(ItemStack stack, LivingEntity entity, int count) {
        if ((entity.isEyeInFluidType(ForgeMod.WATER_TYPE.get()) || this.isSurging) && entity instanceof Player player) {
            if (!player.isCreative())
                if (entity.getHealth() > 1)
                    entity.hurt(SUCC, entity.getMaxHealth() * (float)AbilityUtils.getAbilityValue(stack, "shot", "drain") * 0.02F);
                else
                    entity.kill();

            entity.hurtTime = 0;
            entity.hurtDuration = 0;
            this.isSurging = entity.isInWaterOrRain();

            entity.setSwimming(false);

            Vec3 luk = player.getLookAngle();
            Vec3 motion = entity.getDeltaMovement();
            double spid = AbilityUtils.getAbilityValue(stack, "swim", "speed") / 5;
            AABB aoe = entity.getBoundingBox().inflate(2);

            for(LivingEntity target : entity.getLevel().getEntitiesOfClass(LivingEntity.class, aoe, (living) -> !living.equals(entity) && !(living instanceof LocalPlayer))) {
                target.hurt(DamageSource.playerAttack(player), (float)AbilityUtils.getAbilityValue(stack, "swim", "damage"));
                Vec3 awayctor = target.position().subtract(entity.position()).subtract(motion);
                target.push(awayctor.x() / awayctor.length(), awayctor.y() / awayctor.length(), awayctor.z() / awayctor.length());
            }

            player.setDeltaMovement(0, 0, 0);
            player.push(luk.x() * spid, luk.y() * spid, luk.z() * spid);
            player.startAutoSpinAttack(2);

            for(int i = 0; i < 12; ++i) {
                double a = 30 * i - count * 10;
                double radius = 0.7 + Math.sin(Math.toRadians(count * 20) - 90) * 0.44;

                if (i % 2 == 0)
                    ++radius;

                Vec3 x = motion.normalize().x < 0.001 && motion.normalize().z < 0.001 ? motion.normalize().cross(new Vec3(1, 0, 0)).normalize().scale(radius) : motion.normalize().cross(new Vec3(0, 1, 0)).normalize().scale(radius);
                Vec3 z = motion.normalize().cross(x).normalize().scale(radius);
                Vec3 pos = entity.getPosition(1).add(x.scale(Math.cos(Math.toRadians(a)))).add(z.scale(Math.sin(Math.toRadians(a))));

                if (i % 2 == 0) {
                    pos = pos.add(luk.scale(3.4));

                    if (i % 4 == 0)
                        pos = pos.subtract(luk.scale(0.8));
                }

                pos = pos.add(luk.scale(-0.4));
                entity.getLevel().addParticle(new CircleTintData(new Color(0, (int)(140 + Math.sin(count / 6) * 100), (int)(215 - Math.sin(count / 6) * 40)), 0.35F, 60, 0.92F, false), pos.x(), pos.y(), pos.z(), 0, 0, 0);
            }
        } else if (entity instanceof Player player) {
            if (this.getUseDuration(stack) - count < 19) {
                if (!player.isCreative())
                    if (entity.getHealth() > 1)
                        entity.hurt(SUCC, entity.getMaxHealth() * (float)AbilityUtils.getAbilityValue(stack, "shot", "drain") * (player.isCrouching() ? 0.1F : 0.05F));
                    else
                        entity.kill();

                entity.hurtTime = 0;
                entity.hurtDuration = 0;
            }
        }
    }

    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged;
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment == Enchantments.POWER_ARROWS;
    }

    public int getEnchantmentValue(ItemStack stack) {
        return 20;
    }

    public boolean isEnchantable(ItemStack pStack) {
        return true;
    }

    public UseAnim getUseAnimation(@NotNull ItemStack pStack) {
        return UseAnim.BOW;
    }

    public boolean canContinueUsing(ItemStack oldStack, ItemStack newStack) {
        return true;
    }

    public ItemBorder constructTooltipData() {
        return ItemBorder.builder()
                .backgroundTop(0x021a27)
                .backgroundBottom(0x01141f)
                .borderTop(0xdafffc)
                .borderBottom(0x00aab3)
                .build();
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            final Supplier<MoonBowRenderer> renderer = Suppliers.memoize(MoonBowRenderer::new);

            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return this.renderer.get();
            }
        });
    }

    @SubscribeEvent
    public static void onPlayerHurt(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();

        if (entity instanceof Player player) {
            if (player.isUsingItem()) {
                Item item = player.getUseItem().getItem();

                if (item instanceof MoonBow) {
                    MoonBow bow = (MoonBow)item;

                    if (bow.isSurging && event.getSource() == DamageSource.FALL)
                        event.setCanceled(true);
                }
            }
        }
    }
}
