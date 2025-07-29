package net.xXinailXx.thirteen_flames.item.flame;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import it.hurts.sskirillss.relics.items.relics.base.data.base.RelicData;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityData;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityEntry;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityStat;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicLevelingData;
import it.hurts.sskirillss.relics.items.relics.base.utils.AbilityUtils;
import it.hurts.sskirillss.relics.items.relics.base.utils.LevelingUtils;
import it.hurts.sskirillss.relics.items.relics.base.utils.ResearchUtils;
import it.hurts.sskirillss.relics.utils.MathUtils;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.xXinailXx.enderdragonlib.client.particle.ColoredParticle;
import net.xXinailXx.enderdragonlib.client.particle.ColoredParticleRendererTypes;
import net.xXinailXx.enderdragonlib.client.utils.item.tooltip.ItemBorder;
import net.xXinailXx.enderdragonlib.network.packet.SpawnParticlePacket;
import net.xXinailXx.enderdragonlib.utils.AABBUtils;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.client.renderer.item.EmissiveRenderer;
import net.xXinailXx.thirteen_flames.entity.SoulEntity;
import net.xXinailXx.thirteen_flames.item.base.tools.SwordItemTF;
import net.xXinailXx.thirteen_flames.item.base.tools.ToolTierTF;
import org.zeith.hammerlib.net.Network;

import java.awt.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class StaffJodah extends SwordItemTF {
    public StaffJodah() {
        super(ToolTierTF.THIRTEEN_FLAMES, 3, -2.4F);
    }

    public RelicData getRelicData() {
        return RelicData.builder().abilityData(RelicAbilityData.builder().ability("rage", RelicAbilityEntry.builder().maxLevel(10).stat("procent", RelicAbilityStat.builder().initialValue(1, 10).thresholdValue(1, 50).upgradeModifier(RelicAbilityStat.Operation.ADD, 1).formatValue((value) -> {
            return (int)MathUtils.round(value, 0);
        }).build()).stat("heal", RelicAbilityStat.builder().initialValue(5, 25).thresholdValue(5, 85).upgradeModifier(RelicAbilityStat.Operation.ADD, 5).formatValue((value) -> {
            return (int)MathUtils.round(value, 0);
        }).build()).build()).ability("backlight", RelicAbilityEntry.builder().maxLevel(5).stat("durability", RelicAbilityStat.builder().initialValue(0.5, 1.5).thresholdValue(0.5, 5).upgradeModifier(RelicAbilityStat.Operation.ADD, 0.5).formatValue((value) -> {
            return MathUtils.round(value, 1);
        }).build()).stat("radius", RelicAbilityStat.builder().initialValue(10, 20).thresholdValue(10, 40).upgradeModifier(RelicAbilityStat.Operation.ADD, 2).formatValue((value) -> {
            return (int)MathUtils.round(value, 0);
        }).build()).stat("cooldown", RelicAbilityStat.builder().initialValue(60, 40).thresholdValue(10, 60).upgradeModifier(RelicAbilityStat.Operation.ADD, -5).formatValue((value) -> {
            return (int)MathUtils.round(value, 0);
        }).build()).build()).build()).levelingData(new RelicLevelingData(150, 10, 200)).build();
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!ResearchUtils.isItemResearched(player, stack.getItem()))
            return InteractionResultHolder.success(stack);

        if (hand == InteractionHand.MAIN_HAND && player.isShiftKeyDown() && !AbilityUtils.isAbilityOnCooldown(stack, "backlight")) {
            ColoredParticle.Options options = new ColoredParticle.Options(ColoredParticle.Constructor.builder()
                    .color(new Color(255, 255, 255).getRGB())
                    .renderType(ColoredParticleRendererTypes.RENDER_LIGHT_COLOR)
                    .diameter(0.2F)
                    .lifetime(20)
                    .scaleModifier(0.98F)
                    .physical(false)
                    .build());

            for (int i = 0; i < 360; i++)
                Network.sendTo(new SpawnParticlePacket(options, player.position().x, player.position().y, player.position().z, Math.cos(i), 0, Math.sin(i)), player);

            for (LivingEntity entity : AABBUtils.getEntities(LivingEntity.class, player, AbilityUtils.getAbilityValue(stack,"backlight", "radius"))) {
                if (entity.is(player))
                    continue;

                entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, (int) (20 * AbilityUtils.getAbilityValue(stack,"backlight", "durability")), 1, false, false));

                LevelingUtils.addExperience(stack, 1);
            }

            player.getCooldowns().addCooldown(stack.getItem(), (int) (20 * AbilityUtils.getAbilityValue(stack,"backlight", "cooldown")));
            AbilityUtils.addAbilityCooldown(stack, "backlight", (int) (20 * AbilityUtils.getAbilityValue(stack,"backlight", "cooldown")));
        }

        return super.use(level, player, hand);
    }

    public boolean hurtEnemy(ItemStack stack, LivingEntity entity, LivingEntity living) {
        if (net.xXinailXx.enderdragonlib.utils.MathUtils.isRandom(living.getLevel(), AbilityUtils.getAbilityValue(stack, "rage", "procent"))) {
            float health = (float) (entity.getHealth() * (AbilityUtils.getAbilityValue(stack, "rage", "heal") * 0.01));

            SoulEntity soul = new SoulEntity(living.getLevel(), health);
            soul.setOwner(living);
            soul.setPos(entity.position());
            living.getLevel().addFreshEntity(soul);

            entity.remove(Entity.RemovalReason.KILLED);

            LevelingUtils.addExperience(stack, 2);
        }

        return super.hurtEnemy(stack, entity, living);
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

        if (slot == EquipmentSlot.MAINHAND) {
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, ThirteenFlames.MODID + ":attack_damage", 6, net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, ThirteenFlames.MODID + ":attack_speed", -2.4, net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADDITION));
        }

        return builder.build();
    }

    public ItemBorder constructTooltipData() {
        return ItemBorder.builder()
                .backgroundTop(0x230529)
                .backgroundBottom(0x1b051f)
                .borderTop(0x98dff1)
                .borderBottom(0x1c5f80)
                .build();
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            final Supplier<EmissiveRenderer> renderer = Suppliers.memoize(EmissiveRenderer::new);

            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return this.renderer.get();
            }
        });
    }
}
