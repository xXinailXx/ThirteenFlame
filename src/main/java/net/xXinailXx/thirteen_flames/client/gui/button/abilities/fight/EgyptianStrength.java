package net.xXinailXx.thirteen_flames.client.gui.button.abilities.fight;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.enderdragonlib.capability.manager.TimeManager;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;

@Mod.EventBusSubscriber
public class EgyptianStrength extends AbstarctAbilityWidgets {
    public EgyptianStrength(int x, int y) {
        super(x, y, 8);
    }

    @Override
    public AbilityData constructAbilityData() {
        return AbilityData.builder("egyptian_strength").screenID(ScreenID.FIGHT).requiredLevel(40).build();
    }

    @SubscribeEvent
    public static void addEffectPlayer(TickEvent.PlayerTickEvent event) {
        if (data.isActiveAbility("egyptian_strength")) {
            if (!TimeManager.getTimeMap().containsKey("ability_egyptian_strength"))
                TimeManager.getTimeMap().put("ability_egyptian_strength", 0);

            if (TimeManager.isStop("ability_egyptian_strength")) {
                Player player = event.player;

                if (player == null)
                    return;

                if ((player.getMaxHealth() * 0.3) <= player.getHealth()) {
                    player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 1200, 2));
                    player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 1200, 2));
                    player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 1200, 2));

                    TimeManager.getTimeMap().put("ability_egyptian_strength", 24000);
                }
            }
        }
    }
}
