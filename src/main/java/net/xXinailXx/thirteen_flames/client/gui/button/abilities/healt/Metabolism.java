package net.xXinailXx.thirteen_flames.client.gui.button.abilities.healt;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.ScreenID;

@Mod.EventBusSubscriber
public class Metabolism extends AbstarctAbilityWidgets {
    public Metabolism(int x, int y) {
        super(x, y, 4);
    }

    public AbilityData constructAbilityData() {
        return AbilityData.builder("metabolism").screenID(ScreenID.HEALTH).requiredLevel(40).requiredScarabsForOpen(1).build();
    }

    @SubscribeEvent
    public static void addEffect(TickEvent.PlayerTickEvent event) {
        Player player = event.player;

        if (player == null)
            return;

        if (data.isActiveAbility(player, "metabolism")) {
            if (player.tickCount % 20 == 0) {
                FoodData foodData = player.getFoodData();

                if (player.getHealth() < player.getMaxHealth() && foodData.getFoodLevel() != 0) {
                    foodData.eat(-1, 0);
                    player.heal(1);
                }
            }
        }
    }
}
