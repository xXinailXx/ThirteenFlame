package net.xXinailXx.thirteen_flames.client.gui.button.abilities.healt;

import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.enderdragonlib.capability.managers.UUIDManager;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.ScreenID;

@Mod.EventBusSubscriber
public class ConquerorDunes extends AbstarctAbilityWidgets {
    public ConquerorDunes(int x, int y) {
        super(x, y, 10);
    }

    @Override
    public AbilityData constructAbilityData() {
        return AbilityData.builder("conqueror_dunes").screenID(ScreenID.HEALTH).maxLevel(10).requiredLevel(10).build();
    }

    @SubscribeEvent
    public static void extraSpeed(TickEvent.PlayerTickEvent event) {
        if (data.isActiveAbility("conqueror_dunes")) {
            Player player = event.player;

            if (player == null)
                return;

            AttributeInstance speed = player.getAttribute(Attributes.MOVEMENT_SPEED);
            AttributeModifier bonus = new AttributeModifier(UUIDManager.getOrCreate("tf_ability_conqueror_dunes"), ThirteenFlames.MODID + ":conqueror_dunes", data.getLevelAbility("conqueror_dunes") * 0.01, AttributeModifier.Operation.ADDITION);

            if (player.getMainHandItem().is(Items.AIR) && player.getOffhandItem().is(Items.AIR) || data.getLevelAbility("conqueror_dunes") == bonus.getAmount() * 100) {
                if (!speed.hasModifier(bonus))
                    speed.addTransientModifier(bonus);
            } else {
                speed.removeModifier(bonus);
            }
        }
    }
}
