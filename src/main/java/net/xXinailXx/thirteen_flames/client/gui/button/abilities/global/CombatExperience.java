package net.xXinailXx.thirteen_flames.client.gui.button.abilities.global;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.ScreenID;

@Mod.EventBusSubscriber
public class CombatExperience extends AbstarctAbilityWidgets {
    public CombatExperience(int x, int y) {
        super(x, y, 2);
    }

    public AbilityData constructAbilityData() {
        return AbilityData.builder("combat_experience").screenID(ScreenID.GLOBAL).build();
    }

    @SubscribeEvent
    public static void exchangeXp(TickEvent.PlayerTickEvent event) {
        Player player = event.player;

        if (player == null)
            return;

        if (data.isActiveAbility(player, "combat_experience")) {
            if (player.getHealth() < player.getMaxHealth()) {
                if (player.totalExperience >= 5) {
                    player.giveExperiencePoints(- 5);
                    player.heal(0.5F);
                }
            }
        }
    }
}
