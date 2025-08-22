package net.xXinailXx.thirteen_flames.client.gui.button.abilities.fight;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.enderdragonlib.capability.PlayerCapability;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.ScreenID;

@Mod.EventBusSubscriber
public class EgyptianStrength extends AbstarctAbilityWidgets {
    public EgyptianStrength(int x, int y) {
        super(x, y, 8);
    }

    public AbilityData constructAbilityData() {
        return AbilityData.builder("egyptian_strength").screenID(ScreenID.FIGHT).requiredLevel(40).build();
    }

    @SubscribeEvent
    public static void addEffectPlayer(TickEvent.PlayerTickEvent event) {
        Player player = event.player;

        if (player == null || player.getLevel().isClientSide)
            return;

        if (data.isActiveAbility(player, "egyptian_strength")) {
            if (!PlayerCapability.getTimers(player).contains("tf_ability_egyptian_strength"))
                PlayerCapability.addTimer(player, "tf_ability_egyptian_strength", 0);

            if (PlayerCapability.getTimer(player, "tf_ability_egyptian_strength") == 0) {
                if ((player.getMaxHealth() * 0.3) <= player.getHealth()) {
                    player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 1200, 2, true, true));
                    player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 1200, 2, true, true));
                    player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 1200, 2, true, true));

                    PlayerCapability.addTimer(player, "tf_ability_egyptian_strength", 24000);
                }
            }
        }
    }
}
