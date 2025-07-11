package net.xXinailXx.thirteen_flames.client.gui.button.abilities.global;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.ScreenID;

@Mod.EventBusSubscriber
public class LordElements extends AbstarctAbilityWidgets {
    public LordElements(int x, int y) {
        super(x, y, 5);
    }

    public AbilityData constructAbilityData() {
        return AbilityData.builder("lord_elements").screenID(ScreenID.GLOBAL).build();
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingHurtEvent event) {
        if (event.getEntity() instanceof Player player && event.getSource().isFire())
            if (data.isActiveAbility(player, "lord_elements"))
                event.setCanceled(true);
    }
}
