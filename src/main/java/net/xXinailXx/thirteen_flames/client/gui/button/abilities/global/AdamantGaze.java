package net.xXinailXx.thirteen_flames.client.gui.button.abilities.global;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;

@Mod.EventBusSubscriber
public class AdamantGaze extends AbstarctAbilityWidgets {
    public AdamantGaze(int x, int y) {
        super(x, y, 15 + (effectData.isCurseKnef() ? 1 : 0));
    }

    @Override
    public AbilityData constructAbilityData() {
        return AbilityData.builder("adamant_gaze").screenID(ScreenID.GLOBAL).build();
    }

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;

        if (player == null)
            return;

        if (data.isActiveAbility("adamant_gaze"))
            player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 2, 1, true, true));
    }
}
