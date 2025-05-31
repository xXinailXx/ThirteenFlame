package net.xXinailXx.thirteen_flames.client.gui.button.abilities.fight;

import it.hurts.sskirillss.relics.init.EffectRegistry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityUtils;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.ScreenID;

@Mod.EventBusSubscriber
public class ExcisionFlesh extends AbstarctAbilityWidgets {
    public ExcisionFlesh(int x, int y) {
        super(x, y, 11);
    }

    @Override
    public AbilityData constructAbilityData() {
        return AbilityData.builder("excision_flesh").screenID(ScreenID.FIGHT).maxLevel(20).requiredLevel(5).build();
    }

    @SubscribeEvent
    public static void attackEntity(AttackEntityEvent event) {
        if (data.isActiveAbility("excision_flesh")) {
            if (event.getTarget() instanceof LivingEntity entity) {
                Player player = event.getEntity();

                if (player == null)
                    return;

                if (player.getMainHandItem().getItem() instanceof SwordItem) {
                    if (AbilityUtils.isRandomSuccess(event.getEntity().level, data.getLevelAbility("excision_flesh")))
                        entity.addEffect(new MobEffectInstance(EffectRegistry.BLEEDING.get(), 200));
                }
            }
        }
    }
}
