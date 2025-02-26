package net.xXinailXx.thirteen_flames.client.gui.button.abilities.mining;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityUtils;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;

@Mod.EventBusSubscriber
public class Perseverance extends AbstarctAbilityWidgets {
    public Perseverance(int x, int y) {
        super( x, y, 6);
    }

    @Override
    public AbilityData constructAbilityData() {
        return AbilityData.builder("perseverance").screenID(ScreenID.MINING).maxLevel(5).requiredLevel(50).build();
    }

    @SubscribeEvent
    public static void breakeBlock(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        Level level = event.getPlayer().getLevel();

        if (data.isActiveAbility("perseverance")) {
            if (AbilityUtils.isRandomSuccess(level, data.getLevelAbility("perseverance"))) {
                player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 1000, data.getLevelAbility("perseverance")));
            }
        }
    }
}
