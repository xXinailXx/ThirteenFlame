package net.xXinailXx.thirteen_flames.effect;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.dragonworldlib.capability.manager.UUIDManager;
import net.xXinailXx.dragonworldlib.client.particle.ParticleActions;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityUtils;
import net.xXinailXx.thirteen_flames.init.EffectsRegistry;

import java.awt.*;

@Mod.EventBusSubscriber(modid = ThirteenFlames.MODID)
public class EffectActions {
    @SubscribeEvent
    public static void onLivingHeal(LivingHealEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.hasEffect(EffectsRegistry.ANEMIYA.get())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void doublingXP(PlayerXpEvent.XpChange event) {
        Player player = event.getEntity();
        if (player.hasEffect(EffectsRegistry.BLESSING_HET.get())) {
            event.setAmount(event.getAmount() * 2);
        }
    }

    @SubscribeEvent
    public static void livingHeal(LivingAttackEvent event) {
        if (event.getSource().getEntity() instanceof Player player) {
            if (player.hasEffect(EffectsRegistry.BLESSING_KNEF.get())) {
                if (AbilityUtils.isRandomSuccess(player.getLevel(), 10)) {
                    event.getEntity().hurt(DamageSource.playerAttack(player), 10000000);
                }
            }
        }
    }

    @SubscribeEvent
    public static void effectTick(AttackEntityEvent event) {
        Player player = event.getEntity();

        if (player == null) {
            return;
        }

        if (player.hasEffect(EffectsRegistry.BLESSING_RONOSA.get())) {
            event.getTarget().hurt(DamageSource.playerAttack(player), player.getEffect(EffectsRegistry.BLESSING_RONOSA.get()).getAmplifier());
        }
    }

    @SubscribeEvent
    public static void effectTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;

        if (player == null) {
            return;
        }

        AttributeInstance speed_attribute = player.getAttribute(Attributes.MOVEMENT_SPEED);
        AttributeModifier bonus = new AttributeModifier(UUIDManager.getOrCreate("effect_bless_montu"), ThirteenFlames.MODID + ":effect_bless_montu", 0.2 * (!player.hasEffect(EffectsRegistry.BLESSING_MONTU.get()) ? 1 : player.getEffect(EffectsRegistry.BLESSING_MONTU.get()).getAmplifier()), AttributeModifier.Operation.ADDITION);

        if (player.hasEffect(EffectsRegistry.BLESSING_MONTU.get())) {
            if (!speed_attribute.hasModifier(bonus)) {
                speed_attribute.addTransientModifier(bonus);
            }
        } else {
            speed_attribute.removeModifier(bonus);
        }

        if (player.hasEffect(EffectsRegistry.BLESSING_SELIASET.get())) {
            Level level = player.getLevel();
            BlockPos pos = player.getOnPos();

            BlockPos.betweenClosed(pos.offset(-15, -15, -15), pos.offset(15, 15, 15)).iterator().forEachRemaining(blockPos -> {
                if (AbilityUtils.isRandomSuccess(level, 1) & AbilityUtils.isRandomSuccess(level, 1)) {
                    if (level.getBlockState(blockPos).getBlock() instanceof BonemealableBlock) {
                        BlockState state = level.getBlockState(blockPos);
                        BonemealableBlock bonemealableBlock = (BonemealableBlock) state.getBlock();

                        if (bonemealableBlock.isValidBonemealTarget(level, blockPos, state, level.isClientSide)) {
                            if (!level.isClientSide) {
                                level.levelEvent(1505, blockPos, 0);
                                bonemealableBlock.performBonemeal((ServerLevel) level, level.getRandom(), blockPos, state);
                            }
                        }
                    }
                }
            });
        }
    }
}
