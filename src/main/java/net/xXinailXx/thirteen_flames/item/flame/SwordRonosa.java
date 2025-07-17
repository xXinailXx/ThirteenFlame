package net.xXinailXx.thirteen_flames.item.flame;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import it.hurts.sskirillss.relics.client.particles.circle.CircleTintData;
import it.hurts.sskirillss.relics.client.particles.spark.SparkTintData;
import it.hurts.sskirillss.relics.items.relics.base.data.base.RelicData;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityData;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityEntry;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityStat;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicLevelingData;
import it.hurts.sskirillss.relics.items.relics.base.utils.AbilityUtils;
import it.hurts.sskirillss.relics.items.relics.base.utils.LevelingUtils;
import it.hurts.sskirillss.relics.items.relics.base.utils.ResearchUtils;
import it.hurts.sskirillss.relics.utils.MathUtils;
import it.hurts.sskirillss.relics.utils.Scheduler;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.client.renderer.item.EmissiveRenderer;
import net.xXinailXx.thirteen_flames.entity.PoisonCloundEntity;
import net.xXinailXx.thirteen_flames.init.EffectRegistry;
import net.xXinailXx.thirteen_flames.init.EntityRegistry;
import net.xXinailXx.thirteen_flames.init.ItemRegistry;
import net.xXinailXx.thirteen_flames.item.base.tools.SwordItemTF;
import net.xXinailXx.thirteen_flames.item.base.tools.ToolTierTF;
import org.zeith.hammerlib.util.java.tuples.Tuple3;
import oshi.util.tuples.Pair;

import java.awt.*;
import java.util.HashSet;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Mod.EventBusSubscriber
public class SwordRonosa extends SwordItemTF {
    public SwordRonosa() {
        super(ToolTierTF.THIRTEEN_FLAMES, 3, -2.4F);
    }

    public RelicData getRelicData() {
        return RelicData.builder().abilityData( RelicAbilityData.builder().ability("spit", RelicAbilityEntry.builder().maxLevel(5).stat("range", RelicAbilityStat.builder().initialValue(4, 4.2).thresholdValue(4, 8).upgradeModifier(RelicAbilityStat.Operation.ADD, 0.76).formatValue((value) -> {
            return MathUtils.round(value, 2);
        }).build()).stat("duration", RelicAbilityStat.builder().initialValue(2, 2.5).thresholdValue(2, 4.5).upgradeModifier(RelicAbilityStat.Operation.ADD, 0.4).formatValue((value) -> {
            return MathUtils.round(value, 1);
        }).build()).stat("amplifire", RelicAbilityStat.builder().initialValue(1, 1).thresholdValue(1, 6).upgradeModifier(RelicAbilityStat.Operation.ADD, 1).formatValue((value) -> {
            return MathUtils.round(value, 1);
        }).build()).build()).ability("fart", RelicAbilityEntry.builder().maxLevel(3).stat("duration", RelicAbilityStat.builder().initialValue(6, 10).thresholdValue(6, 25).upgradeModifier( RelicAbilityStat.Operation.ADD, 5).formatValue((value) -> {
            return MathUtils.round(value, 1);
        }).build()).stat("cooldown", RelicAbilityStat.builder().initialValue(30, 40).thresholdValue(12, 40).upgradeModifier(RelicAbilityStat.Operation.MULTIPLY_TOTAL, -6).formatValue((value) -> {
            return MathUtils.round(value, 1);
        }).build()).stat("radius", RelicAbilityStat.builder().initialValue(1, 2).thresholdValue(1, 4).upgradeModifier(RelicAbilityStat.Operation.ADD, 0.67).formatValue((value) -> {
            return MathUtils.round(value, 1);
        }).build()).build()).ability("anemia", RelicAbilityEntry.builder().maxLevel(5).stat("level", RelicAbilityStat.builder().initialValue(0, 0).upgradeModifier(RelicAbilityStat.Operation.ADD, 1.0).formatValue((value) -> {
            return (int) MathUtils.round(value, 0);
        }).build()).build()).build()).levelingData(new RelicLevelingData(100, 10, 100)).build();
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (! ResearchUtils.isItemResearched(player, player.getItemInHand(hand).getItem()))
            return InteractionResultHolder.success(player.getItemInHand(hand));

        if (player.isCrouching() && !AbilityUtils.isAbilityOnCooldown(player.getItemInHand(hand), "fart")) {
            level.playSound(null, player, SoundEvents.CHORUS_FLOWER_DEATH, SoundSource.MASTER, 1.2F, 0.1F);
            level.playSound(null, player, SoundEvents.SCULK_BLOCK_BREAK, SoundSource.MASTER, 1.0F, 1.0F);
            level.playSound(null, player, SoundEvents.AZALEA_FALL, SoundSource.MASTER, 1.0F, 0.01F);

            ItemStack sword = player.getItemInHand(hand);
            int lifetime = (int)AbilityUtils.getAbilityValue(sword, "fart", "duration") * 20;
            float radius = (float)AbilityUtils.getAbilityValue(sword, "fart", "radius");

            PoisonCloundEntity cloud = new PoisonCloundEntity(EntityRegistry.POISON_CLOUD.get(), level);
            cloud.setRadius(radius);
            cloud.setLifeTime(lifetime);
            cloud.setAmplifire((int)Math.round(AbilityUtils.getAbilityValue(sword, "spit", "amplifire") - 1.0F));
            cloud.setDuration((int)Math.round(AbilityUtils.getAbilityValue(sword, "spit", "duration") * 20.0F));
            cloud.setOwner(player);
            cloud.setSword(player.getItemInHand(hand));

            Vec3 pos = player.getEyePosition(1.0F).add(player.getLookAngle().scale((radius + 1.0F)));
            cloud.setPos(pos);
            level.addFreshEntity(cloud);

            AbilityUtils.addAbilityCooldown(player.getItemInHand(hand), "fart", (int)AbilityUtils.getAbilityValue(sword, "fart", "cooldown") * 20);
        }

        return super.use(level, player, hand);
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

        if (slot == EquipmentSlot.MAINHAND) {
            float level = (float)AbilityUtils.getAbilityValue(stack, "anemia", "level");

            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, ThirteenFlames.MODID + ":attack_damage", 7, net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, ThirteenFlames.MODID + ":attack_speed", (-2.4 + level), net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADDITION));
        }

        return builder.build();
    }

    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged;
    }

    public boolean hurtEnemy(ItemStack stack, LivingEntity living, LivingEntity entity) {
        living.addEffect(new MobEffectInstance(MobEffects.POISON, 100), entity);

        return super.hurtEnemy(stack, living, entity);
    }

    @SubscribeEvent
    public static void attackEntity(AttackEntityEvent event) {
        if (event.getEntity().getMainHandItem().is(ItemRegistry.SWORD_RONOSA.get()) && event.getTarget() instanceof LivingEntity living) {
            poisonSwipe(event.getEntity(), event.getEntity().getMainHandItem());
            living.addEffect(new MobEffectInstance(EffectRegistry.POISON.get(), 100, 0, false, true, false));
        }
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment.category == EnchantmentCategory.WEAPON;
    }

    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slot, isSelected);

        if (entity instanceof Player player) {
            if (stack.is(this) && (!player.hasEffect(EffectRegistry.ANEMIA.get()) || player.hasEffect(EffectRegistry.ANEMIA.get()) && player.getEffect(EffectRegistry.ANEMIA.get()).getDuration() < 20) || AbilityUtils.getAbilityValue(stack, "anemia", "level") < 5) {
                player.addEffect(new MobEffectInstance(EffectRegistry.ANEMIA.get(), 1, 0, true, false, true));
            }
        }
    }

    public static void poisonSwipe(LivingEntity p, ItemStack sword) {
        p.level.playSound((Player)null, p, SoundEvents.AZALEA_FALL, SoundSource.MASTER, 1.0F, 0.02F);
        p.level.playSound((Player)null, p, SoundEvents.SCULK_BLOCK_BREAK, SoundSource.MASTER, 1.0F, 1.0F);
        p.level.playSound((Player)null, p, SoundEvents.AZALEA_LEAVES_FALL, SoundSource.MASTER, 1.0F, 1.8F);

        double spreadAngle = 20.0F + AbilityUtils.getAbilityValue(sword, "spit", "range") * 1.8;
        double range = AbilityUtils.getAbilityValue(sword, "spit", "range");
        int maxAmp = (int)Math.round(AbilityUtils.getAbilityValue(sword, "spit", "amplifire") - 1.0F);
        Vec3 startVec = p.getEyePosition(1.0F).add(0.0F, -0.2, 0.0F);
        Vec3 luk = Vec3.directionFromRotation(0.0F, p.getYHeadRot());
        Vec3 down = p.getLookAngle().subtract(luk);
        Level entitySet = p.level;

        if (entitySet instanceof ServerLevel level) {
            for(int i = 0; i < range * 1.8; ++i) {
                int dark = RandomSource.create().nextInt(80);
                int yellowness = RandomSource.create().nextInt(80);

                int finalI = i;

                Scheduler.schedule(i, () -> {
                    for(int j = 0; j < range * 4.0F + 1.0F; ++j) {
                        Vec3 vec = startVec.add(luk.yRot((float)Math.toRadians(-spreadAngle + j * (spreadAngle * 2.0F / range / 4.0F))).add(down).normalize().scale(0.7 + finalI / 1.8));
                        level.sendParticles(new CircleTintData(new Color(85 - dark + yellowness, 255 - dark - RandomSource.create().nextInt(100), 0), (float)(0.2F + 0.025F * range), 20, 0.83F, false), vec.x, vec.y, vec.z, 1, 0.018 * range, 0.018 * range, 0.018 * range, 0.005 + finalI * 0.008);

                        if (j % 3 == 0)
                            level.sendParticles(new SparkTintData(new Color(85 - RandomSource.create().nextInt(80), 255 - RandomSource.create().nextInt(100), 0), (float)(0.2F + 0.025F * range), 20), vec.x, vec.y, vec.z, 1, 0.018 * range, 0.018 * range, 0.018 * range, 0.005 + finalI * 0.008);
                    }
                });
            }
        }

        AABB eBox = (new AABB(startVec.add(p.getLookAngle().scale(range * 0.6)), startVec.add(p.getLookAngle().scale(range * 0.6)))).inflate(range * 0.3);
        HashSet<LivingEntity> hashSet = new HashSet(p.level.getEntitiesOfClass(LivingEntity.class, eBox, (entity) -> !entity.equals(p)));
        eBox = (new AABB(startVec.add(p.getLookAngle().scale(range * 0.2)), startVec.add(p.getLookAngle().scale(range * 0.2)))).inflate(range * 0.1);
        hashSet.addAll(p.level.getEntitiesOfClass(LivingEntity.class, eBox, (ex) -> !ex.equals(p)));
        int duration = (int)Math.round(AbilityUtils.getAbilityValue(sword, "spit", "duration") * 20.0F);
        RandomSource random = p.getRandom();

        for(LivingEntity e : hashSet) {
            e.hurt(DamageSource.mobAttack(p), 1.0F);

            if (e.hasEffect(EffectRegistry.POISON.get())) {
                int appliedAmplifier = e.getEffect(EffectRegistry.POISON.get()).getAmplifier() + 1;

                if (appliedAmplifier <= maxAmp) {
                    e.addEffect(new MobEffectInstance(EffectRegistry.POISON.get(), duration + appliedAmplifier * 20, appliedAmplifier, false, true));

                    if (random.nextFloat() < 0.25F)
                        LevelingUtils.addExperience(sword, 1);
                } else {
                    e.addEffect(new MobEffectInstance(EffectRegistry.POISON.get(), duration + maxAmp * 20, maxAmp, false, true, false));
                }
            } else {
                e.addEffect(new MobEffectInstance(EffectRegistry.POISON.get(), duration, 0, false, true, false));

                if (random.nextFloat() < 0.25F)
                    LevelingUtils.addExperience(sword, 1);
            }
        }

    }

    protected Pair<Tuple3<Float, Float, Float>, Vec3> beamSetting() {
        return new Pair<>(new Tuple3<>(1F, 1F, 1F), new Vec3(0, 0.45, 0));
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            final Supplier<EmissiveRenderer> renderer = Suppliers.memoize(EmissiveRenderer::new);

            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return (BlockEntityWithoutLevelRenderer)this.renderer.get();
            }
        });
    }

    @SubscribeEvent
    public static void tossItem(ItemTossEvent event) {
        if (event.getPlayer().isCreative())
            return;

        ItemStack stack = event.getEntity().getItem();

        if (!(stack.getItem() instanceof SwordRonosa))
            return;

        double anemia = AbilityUtils.getAbilityPoints(stack, "anemia");

        if (anemia < 5) {
            if (event.getEntity().getItem().is(ItemRegistry.SWORD_RONOSA.get())) {
                event.getEntity().setItem(Items.AIR.getDefaultInstance());
                event.getPlayer().addItem(new ItemStack(ItemRegistry.SWORD_RONOSA.get()));
            }
        }
    }
}
