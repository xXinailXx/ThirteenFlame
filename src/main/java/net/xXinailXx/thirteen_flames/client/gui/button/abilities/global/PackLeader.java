package net.xXinailXx.thirteen_flames.client.gui.button.abilities.global;

import it.hurts.sskirillss.relics.init.ItemRegistry;
import it.hurts.sskirillss.relics.utils.EntityUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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

    @Override
    public AbilityData constructAbilityData() {
        return AbilityData.builder("pack_leader").screenID(ScreenID.GLOBAL).build();
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingHurtEvent event) {
        if (data.isActiveAbility("pack_leader")) {
            if (event.getEntity() instanceof TamableAnimal pet) {
                if (pet.getOwner() instanceof Player player)
                    event.setAmount(0);
            }
        }
    }
}
