package net.xXinailXx.thirteen_flames.client.gui.button.abilities.healt;

import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.enderdragonlib.capability.managers.UUIDManager;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.ScreenID;
import net.xXinailXx.thirteen_flames.data.Data;
import net.xXinailXx.thirteen_flames.data.StaminaData;

@Mod.EventBusSubscriber
public class Resistance extends AbstarctAbilityWidgets {
    public Resistance(int x, int y) {
        super(x, y, 3);
    }

    @Override
    public AbilityData constructAbilityData() {
        return AbilityData.builder("resistance").screenID(ScreenID.HEALTH).requiredLevel(60).requiredScarabsForOpen(2).build();
    }

    @SubscribeEvent
    public static void addEffect(TickEvent.PlayerTickEvent event) {
        Player player = event.player;

        if (player == null)
            return;

        AttributeInstance damage = player.getAttribute(Attributes.ATTACK_DAMAGE);
        AttributeModifier bonusDamage = new AttributeModifier(UUIDManager.getOrCreate("ability_damage_bonus"), ThirteenFlames.MODID + ":damage_bonus", damage.getValue(), AttributeModifier.Operation.ADDITION);

        if (data.isActiveAbility("resistance") && StaminaData.Utils.getStaminaData(player).isStaminaFull()) {
            if (!damage.hasModifier(bonusDamage))
                damage.addTransientModifier(bonusDamage);
        } else {
            damage.removeModifier(bonusDamage);
        }
    }
}
