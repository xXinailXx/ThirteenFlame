package net.xXinailXx.thirteen_flames.client.gui.button.abilities.global;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;

@Mod.EventBusSubscriber
public class CoverNight extends AbstarctAbilityWidgets {
    private static int tickState;
    private static Vec3 lastPos;

    static {
        tickState = 6;
        lastPos = Vec3.ZERO;
    }

    public CoverNight(int x, int y) {
        super(x, y, 9 + (effectData.isCurseKnef() ? 1 : 0));
    }

    @Override
    public AbilityData constructAbilityData() {
        return AbilityData.builder("cover_night").screenID(ScreenID.GLOBAL).build();
    }

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;

        if (player == null) {
            return;
        }

        if (data.isActiveAbility("cover_night")) {
            if (player.hasEffect(MobEffects.INVISIBILITY) && player.hasEffect(MobEffects.REGENERATION)) {
                return;
            }

            if (tickState == 0) {
                if (!player.hasEffect(MobEffects.INVISIBILITY)) {
                    player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 300, 1, true, true));
                }

                if (!player.hasEffect(MobEffects.REGENERATION)) {
                    player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 300, 2, true, true));
                }

                tickState = 6;
                return;
            } else {
                Vec3 playerPos = player.position();

                if (playerPos.distanceTo(lastPos) != 0) {
                    lastPos = playerPos;
                    tickState = 6;
                    return;
                }

                if (!player.getLevel().isClientSide) {
                    if (player.tickCount % 20 == 0) {
                        tickState--;
                    }
                }
            }
        } else {
            tickState = 6;
        }
    }
}
