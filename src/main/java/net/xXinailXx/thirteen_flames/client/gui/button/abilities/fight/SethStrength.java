package net.xXinailXx.thirteen_flames.client.gui.button.abilities.fight;

import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.dragonworldlib.capability.manager.UUIDManager;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;

@Mod.EventBusSubscriber
public class SethStrength extends AbstarctAbilityWidgets {
    public SethStrength(int x, int y) {
        super(x, y, 3);
    }

    @Override
    public AbilityData constructAbilityData() {
        return AbilityData.builder("seth_strength").screenID(ScreenID.FIGHT).requiredLevel(100).build();
    }

    @SubscribeEvent
    public static void addStrength(TickEvent.PlayerTickEvent event) {
        Player player = event.player;

        if (player == null) {
            return;
        }

        AttributeModifier bonus = new AttributeModifier(UUIDManager.getOrCreate("ability_seth_strength"), ThirteenFlames.MODID + ":seth_strength", (player.getMaxHealth() * 0.05F), AttributeModifier.Operation.ADDITION);
        AttributeInstance strength = player.getAttribute(Attributes.ATTACK_DAMAGE);

        if (data.isActiveAbility("seth_strength")) {
            if (!strength.hasModifier(bonus)) {
                strength.addTransientModifier(bonus);
            }
        } else {
            strength.removeModifier(bonus);
        }
    }
}
