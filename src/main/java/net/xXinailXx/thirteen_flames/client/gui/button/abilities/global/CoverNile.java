package net.xXinailXx.thirteen_flames.client.gui.button.abilities.global;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.ScreenID;

@Mod.EventBusSubscriber
public class CoverNile extends AbstarctAbilityWidgets {
    public CoverNile(int x, int y) {
        super(x, y, 0);
    }

    public AbilityData constructAbilityData() {
        return AbilityData.builder("cover_nile").screenID(ScreenID.GLOBAL).build();
    }

    @SubscribeEvent
    public static void attackEntity(LivingHurtEvent event) {
        if (event.getEntity() instanceof Player player && player.isUnderWater())
            if (data.isActiveAbility(player, "cover_nile"))
                event.setCanceled(true);
    }
}
