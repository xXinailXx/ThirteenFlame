package net.xXinailXx.thirteen_flames.client.gui.button.abilities.fight;

import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.AxeItem;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityUtils;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.ScreenID;

@Mod.EventBusSubscriber
public class Confidence extends AbstarctAbilityWidgets {
    public Confidence(int x, int y) {
        super(x, y, 14);
    }

    @Override
    public AbilityData constructAbilityData() {
        return AbilityData.builder("confidence").screenID(ScreenID.FIGHT).maxLevel(5).requiredLevel(40).requiredScarabsForUpgrade(2).requiredScarabsForOpen(2).build();
    }

    @SubscribeEvent
    public static void attackEntity(AttackEntityEvent event) {
        if (data.isActiveAbility("confidence")) {
            if (event.getEntity().getMainHandItem().getItem() instanceof AxeItem) {
                if (AbilityUtils.isRandomSuccess(event.getEntity().getLevel(), data.getLevelAbility("confidence"))) {
                    LivingEntity entity = (LivingEntity) event.getTarget();
                    entity.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 200, 2));
                    entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 2));
                    entity.knockback(1.5D, (double) Mth.sin(event.getEntity().getYRot() * ((float)Math.PI / 180F)), (double)(-Mth.cos(event.getEntity().getYRot() * ((float)Math.PI / 180F))));
                }
            }
        }
    }
}
