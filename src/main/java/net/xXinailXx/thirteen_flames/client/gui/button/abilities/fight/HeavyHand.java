package net.xXinailXx.thirteen_flames.client.gui.button.abilities.fight;

import it.hurts.sskirillss.relics.init.EffectRegistry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityUtils;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;

@Mod.EventBusSubscriber
public class HeavyHand extends AbstarctAbilityWidgets {
    public HeavyHand(int x, int y) {
        super(x, y, 12);
    }

    @Override
    public AbilityData constructAbilityData() {
        return AbilityData.builder("heavy_hand").screenID(ScreenID.FIGHT).maxLevel(10).requiredLevel(10).build();
    }

    @SubscribeEvent
    public static void addStun(AttackEntityEvent event) {
        if (data.isActiveAbility("heavy_hand")) {
            if (event.getTarget() instanceof LivingEntity entity) {
                Player player = event.getEntity();

                if (player == null)
                    return;

                if (player.getMainHandItem().getItem() instanceof AxeItem) {
                    if (AbilityUtils.isRandomSuccess(player.getLevel(), data.getLevelAbility("heavy_hand")))
                        entity.addEffect(new MobEffectInstance(EffectRegistry.STUN.get(), 30));
                }
            }
        }
    }
}
