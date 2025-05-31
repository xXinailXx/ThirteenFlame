package net.xXinailXx.thirteen_flames.client.gui.button.abilities.global;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityUtils;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.ScreenID;

@Mod.EventBusSubscriber
public class GehennaFire extends AbstarctAbilityWidgets {
    public GehennaFire(int x, int y) {
        super(x, y, 14 + (effectData.isCurseKnef() ? 1 : 0));
    }

    @Override
    public AbilityData constructAbilityData() {
        return AbilityData.builder("gehenna_fire").screenID(ScreenID.GLOBAL).build();
    }

    @SubscribeEvent
    public static void deathEntity(LivingDeathEvent event) {
        if (data.isActiveAbility("cover_night")) {
            if (event.getEntity() instanceof WitherSkeleton && event.getSource().getEntity() instanceof Player player) {
                if (AbilityUtils.isRandomSuccess(player.getLevel(), 50)) {
                    WitherBoss wither = EntityType.WITHER.create(player.getLevel());
                    wither.setPos(event.getEntity().position());
                    player.getLevel().addFreshEntity(wither);
                }
            }
        }
    }
}
