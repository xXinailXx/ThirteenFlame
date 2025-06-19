package net.xXinailXx.thirteen_flames.effect;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.enderdragonlib.capability.managers.UUIDManager;
import net.xXinailXx.enderdragonlib.client.particle.ColoredParticle;
import net.xXinailXx.enderdragonlib.network.packet.SpawnParticlePacket;
import net.xXinailXx.enderdragonlib.utils.MathUtils;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.init.EffectRegistry;
import net.xXinailXx.thirteen_flames.utils.ParticleUtils;
import org.zeith.hammerlib.net.Network;

import java.awt.*;

@Mod.EventBusSubscriber(modid = ThirteenFlames.MODID)
public class EffectActions {
    @SubscribeEvent
    public static void onLivingHeal(LivingHealEvent event) {
        LivingEntity entity = event.getEntity();

        if (entity.hasEffect(EffectRegistry.ANEMIA.get())) {
            int amp = event.getEntity().getEffect(EffectRegistry.ANEMIA.get()).getAmplifier() + 1;

            event.setAmount(event.getAmount() * (0.8F / (float)amp));
        }
    }

    @SubscribeEvent
    public static void doublingXP(PlayerXpEvent.XpChange event) {
        Player player = event.getEntity();

        if (player.hasEffect(EffectRegistry.BLESSING_HET.get()))
            if (MathUtils.isRandom(player.getLevel(), Math.round(1 + (0.39F * player.getEffect(EffectRegistry.BLESSING_HET.get()).getAmplifier()))))
                event.setAmount(event.getAmount() * 2);
    }

    @SubscribeEvent
    public static void livingHeal(LivingAttackEvent event) {
        if (event.getSource().getEntity() instanceof Player player) {
            if (player.hasEffect(EffectRegistry.BLESSING_KNEF.get())) {
                if (MathUtils.isRandom(player.getLevel(), Math.round(1 + (0.39F * player.getEffect(EffectRegistry.BLESSING_KNEF.get()).getAmplifier()))))
                    event.getEntity().hurt(DamageSource.playerAttack(player), 10000000);
            }
        }
    }

    @SubscribeEvent
    public static void effectTick(AttackEntityEvent event) {
        Player player = event.getEntity();

        if (player == null)
            return;

        if (player.hasEffect(EffectRegistry.BLESSING_RONOSA.get()))
            event.getTarget().hurt(DamageSource.playerAttack(player), 0.5F * player.getEffect(EffectRegistry.BLESSING_RONOSA.get()).getAmplifier());
    }

    @SubscribeEvent
    public static void effectTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;

        if (player == null)
            return;

        AttributeInstance speed_attribute = player.getAttribute(Attributes.MOVEMENT_SPEED);
        AttributeModifier bonus = new AttributeModifier(UUIDManager.getOrCreate("tf_effect_bless_montu"), ThirteenFlames.MODID + ":effect_bless_montu", player.hasEffect(EffectRegistry.BLESSING_MONTU.get()) ? (0.01 * player.getEffect(EffectRegistry.BLESSING_MONTU.get()).getAmplifier()) : 0, AttributeModifier.Operation.ADDITION);

        if (player.hasEffect(EffectRegistry.BLESSING_MONTU.get())) {
            if (!speed_attribute.hasModifier(bonus))
                speed_attribute.addTransientModifier(bonus);
        } else {
            speed_attribute.removeModifier(bonus);
        }

        if (player.hasEffect(EffectRegistry.BLESSING_SELIASET.get())) {
            Level level = player.getLevel();
            BlockPos pos = player.getOnPos();

            BlockPos.betweenClosed(pos.offset(-15, -15, -15), pos.offset(15, 15, 15)).iterator().forEachRemaining(blockPos -> {
                int amplifire = player.getEffect(EffectRegistry.BLESSING_SELIASET.get()).getAmplifier();

                if (0.39F * amplifire > 1) {
                    if (MathUtils.isRandom(level, (int) (1 + (0.39F * amplifire))) && MathUtils.isRandom(level, (int) (1 + (0.39F * amplifire)))) {
                        if (level.getBlockState(blockPos).getBlock() instanceof BonemealableBlock) {
                            BlockState state = level.getBlockState(blockPos);
                            BonemealableBlock bonemealableBlock = (BonemealableBlock) state.getBlock();

                            if (bonemealableBlock.isValidBonemealTarget(level, blockPos, state, level.isClientSide)) {
                                if (! level.isClientSide) {
                                    level.levelEvent(1505, blockPos, 0);
                                    bonemealableBlock.performBonemeal((ServerLevel) level, level.getRandom(), blockPos, state);

                                    Color color = new Color(255, 203, 0);

                                    ColoredParticle.Options options = ParticleUtils.createStatueParticle(color, 0.25F, 40, 0.95F);

                                    Network.sendToAll(new SpawnParticlePacket(options, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 0, 0, 0));
                                }
                            }
                        }
                    }
                }
            });
        }
    }
}
