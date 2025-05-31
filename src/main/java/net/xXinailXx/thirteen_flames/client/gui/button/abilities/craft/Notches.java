package net.xXinailXx.thirteen_flames.client.gui.button.abilities.craft;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.ScreenID;

@Mod.EventBusSubscriber
public class Notches extends AbstarctAbilityWidgets {
    public Notches(int x, int y) {
        super(x, y, 0);
    }

    @Override
    public AbilityData constructAbilityData() {
        return AbilityData.builder("notches").screenID(ScreenID.CRAFT).maxLevel(5).requiredLevel(100).requiredScarabsForOpen(5).build();
    }

    @SubscribeEvent
    public static void attackEntity(AttackEntityEvent event) {
        if (data.isActiveAbility("notches")) {
            Player player = event.getEntity();

            if (player != null) {
                if (player.getMainHandItem().getItem() != null) {
                    ItemStack item = player.getMainHandItem();

                    int maxDurability = item.getMaxDamage();
                    int durability = item.getDamageValue();

                    if (item.isDamaged()) {
                        float extraDamage = (float) ((maxDurability - durability) * (data.getLevelAbility("notches") * 0.001));

                        event.getTarget().hurt(DamageSource.playerAttack(player), extraDamage);
                    }
                }
            }
        }
    }
}
