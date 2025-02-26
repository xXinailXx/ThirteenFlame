package net.xXinailXx.thirteen_flames.client.gui.button.abilities.fight;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;

@Mod.EventBusSubscriber
public class PharaohStrength extends AbstarctAbilityWidgets {
    public PharaohStrength(int x, int y) {
        super(x, y, 10);
    }

    @Override
    public AbilityData constructAbilityData() {
        return AbilityData.builder("pharaoh_strength").screenID(ScreenID.FIGHT).maxLevel(5).requiredLevel(60).requiredScarabsForOpen(2).requiredScarabsForUpgrade(2).build();
    }

    @SubscribeEvent
    public static void addEffectPlayer(AttackEntityEvent event) {
        if (data.isActiveAbility("pharaoh_strength")) {
            Player player = event.getEntity();

            if (player == null) {
                return;
            }

            if (player.getMainHandItem().getItem() instanceof AxeItem) {
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 1200, data.getLevelAbility("pharaoh_strength") - 1));
            }
        }
    }
}
