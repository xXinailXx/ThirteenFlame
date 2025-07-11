package net.xXinailXx.thirteen_flames.client.gui.button.abilities.fight;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityUtils;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.ScreenID;

@Mod.EventBusSubscriber
public class StrikingZeal extends AbstarctAbilityWidgets {
    public StrikingZeal(int x, int y) {
        super(x, y, 1);
    }

    public AbilityData constructAbilityData() {
        return AbilityData.builder("striking_zeal").screenID(ScreenID.FIGHT).requiredLevel(100).requiredScarabsForOpen(5).build();
    }

    @SubscribeEvent
    public static void attackEntity(AttackEntityEvent event) {
        Player player = event.getEntity();

        if (player == null)
            return;

        if (data.isActiveAbility(player, "striking_zeal")) {
            if (!(player.getMainHandItem().getItem() instanceof SwordItem))
                return;

            AbilityUtils.getEntities((LivingEntity) event.getTarget(), 2.5).forEach(entity -> {
                entity.hurt(DamageSource.playerAttack(player), 5);
            });

            BlockPos.betweenClosed(new BlockPos(event.getTarget().getX() + 2.5, event.getTarget().getY() + 1, event.getTarget().getZ() - 2.5)
                    , new BlockPos(event.getTarget().getX() - 2.5, event.getTarget().getY() + 1, event.getTarget().getZ() + 2.5)).forEach(pos -> {
                Level level = event.getTarget().getLevel();

                level.addParticle(ParticleTypes.SWEEP_ATTACK, pos.getX(), pos.getY() , pos.getZ(), 0, 0, 0);
            });
        }
    }
}
