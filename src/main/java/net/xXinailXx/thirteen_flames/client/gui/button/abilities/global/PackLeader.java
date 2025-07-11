package net.xXinailXx.thirteen_flames.client.gui.button.abilities.global;

import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.ScreenID;

@Mod.EventBusSubscriber
public class PackLeader extends AbstarctAbilityWidgets {
    public PackLeader(int x, int y) {
        super(x, y, 3);
    }

    public AbilityData constructAbilityData() {
        return AbilityData.builder("pack_leader").screenID(ScreenID.GLOBAL).build();
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingHurtEvent event) {
        if (event.getEntity() instanceof TamableAnimal pet)
            if (pet.getOwner() instanceof Player player)
                if (data.isActiveAbility(player, "pack_leader"))
                    event.setAmount(0);
    }
}
