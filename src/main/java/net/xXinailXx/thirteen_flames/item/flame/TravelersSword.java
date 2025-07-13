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
import it.hurts.sskirillss.relics.utils.MathUtils;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.enderdragonlib.capability.managers.UUIDManager;
import net.xXinailXx.enderdragonlib.client.particle.ColoredParticle;
import net.xXinailXx.enderdragonlib.client.particle.ColoredParticleRendererTypes;
import net.xXinailXx.enderdragonlib.network.packet.SpawnParticlePacket;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.client.renderer.item.EmissiveRenderer;
import net.xXinailXx.thirteen_flames.init.ItemRegistry;
import net.xXinailXx.thirteen_flames.item.base.tools.SwordItemTF;
import net.xXinailXx.thirteen_flames.item.base.tools.ToolTierTF;
import org.zeith.hammerlib.net.Network;
import org.zeith.hammerlib.util.java.tuples.Tuple3;
import oshi.util.tuples.Pair;

import java.awt.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Mod.EventBusSubscriber
public class TravelersSword extends SwordItemTF {
    public TravelersSword() {
        super(ToolTierTF.THIRTEEN_FLAMES, 7, -2.4F);
    }

    public RelicData getRelicData() {
        return RelicData.builder().abilityData(RelicAbilityData.builder().ability("wind_wandering", RelicAbilityEntry.builder().maxLevel(5).stat("speed", RelicAbilityStat.builder().initialValue(3.6, 9.7).thresholdValue(3.6, 15).upgradeModifier(RelicAbilityStat.Operation.ADD, 1).formatValue((value) -> {
            return MathUtils.round(value, 1);
        }).build()).build()).ability("wide_step", RelicAbilityEntry.builder().maxLevel(5).stat("climbing_height", RelicAbilityStat.builder().initialValue(1, 4).thresholdValue(1, 6.5).upgradeModifier(RelicAbilityStat.Operation.ADD, 0.4).formatValue((value) -> {
            return MathUtils.round(value, 1);
        }).build()).stat("speed", RelicAbilityStat.builder().initialValue(3, 5).thresholdValue(3, 15).upgradeModifier(RelicAbilityStat.Operation.ADD, 1).formatValue((value) -> {
            return (int)MathUtils.round(value, 0);
        }).build()).build()).build()).levelingData(new RelicLevelingData(100, 10, 150)).build();
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (hand.equals(InteractionHand.MAIN_HAND)) {
            ItemStack stack = player.getMainHandItem();
            double speedValue = AbilityUtils.getAbilityValue(stack, "wind_wandering", "speed");
            Vec3 look = player.getLookAngle().normalize();
            double lookX = look.x;
            double lookY = look.y;
            double lookZ = look.z;

            player.setDeltaMovement(lookX * (speedValue * 0.1), lookY  * (speedValue * 0.1), lookZ  * (speedValue * 0.1));

            Vec3 motion = player.getDeltaMovement();
            ColoredParticle.Options particle = new ColoredParticle.Options(ColoredParticle.Constructor.builder()
                    .color(new Color(255, 255, 255).getRGB())
                    .renderType(ColoredParticleRendererTypes.RENDER_LIGHT_COLOR)
                    .diameter(0.1F)
                    .lifetime(1)
                    .scaleModifier(1F)
                    .physical(false)
                    .build());

            for (int i = 0; i < 360; i++) {
                if (i % 10 == 0) {
                    double a = 36D * i - i;
                    double radius = 1;

                    Vec3 x = motion.normalize().cross(new Vec3(1, 0, 0)).normalize().scale(radius);
                    Vec3 z = motion.normalize().cross(x).normalize().scale(radius);
                    Vec3 pos = player.getEyePosition(1).add(x.scale(Math.cos(Math.toRadians(a)))).add(z.scale(Math.sin(Math.toRadians(a)))).add(player.getLookAngle().normalize().scale(3));

                    Network.sendToAll(new SpawnParticlePacket(particle, pos.x, pos.y, pos.z, 0, 0, 0));
                }
            }

            LevelingUtils.addExperience(stack, 1);
        }

        return super.use(level, player, hand);
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

        if (slot == EquipmentSlot.MAINHAND) {
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, ThirteenFlames.MODID + ":attack_damage", 8, net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, ThirteenFlames.MODID + ":attack_speed", 0.7, net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADDITION));
        }

        return builder.build();
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            final Supplier<EmissiveRenderer> renderer = Suppliers.memoize(EmissiveRenderer::new);

            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return (BlockEntityWithoutLevelRenderer)this.renderer.get();
            }
        });
    }

    protected Pair<Tuple3<Float, Float, Float>, Vec3> beamSetting() {
        return new Pair<>(new Tuple3<>(1F, 1F, 1F), new Vec3(0, 0.5, 0));
    }

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;

        if (player == null)
            return;

        ItemStack stack = player.getMainHandItem();

        double clumbHeight = AbilityUtils.getAbilityValue(stack, "wide_step", "climbing_height");
        AttributeInstance stepHeight = player.getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get());
        AttributeModifier stepHeightBonus = new AttributeModifier(UUIDManager.getOrCreate("tf_travel_sword_step_height"), ThirteenFlames.MODID + ":ts_step_height_bonus", clumbHeight, AttributeModifier.Operation.ADDITION);

        double speed = AbilityUtils.getAbilityValue(stack, "wide_step", "speed");
        AttributeModifier speedBonus = new AttributeModifier(UUIDManager.getOrCreate("tf_travel_sword_speed"), ThirteenFlames.MODID + ":ts_speed_bonus", (speed - 1D) / 2, AttributeModifier.Operation.MULTIPLY_BASE);
        AttributeInstance movementSpeed = player.getAttribute(Attributes.MOVEMENT_SPEED);

        if (stack.is(ItemRegistry.TRAVELERS_SWORD.get())) {
            if (!movementSpeed.hasModifier(speedBonus))
                movementSpeed.addTransientModifier(speedBonus);

            if (!stepHeight.hasModifier(stepHeightBonus))
                stepHeight.addTransientModifier(stepHeightBonus);
        } else {
            stepHeight.removeModifier(stepHeightBonus);
            movementSpeed.removeModifier(speedBonus);
        }
    }
}