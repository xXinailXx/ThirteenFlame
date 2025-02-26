package net.xXinailXx.thirteen_flames.client.gui.button.abilities.global;

import it.hurts.sskirillss.relics.api.events.common.FluidCollisionEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;

@Mod.EventBusSubscriber
public class NileFlame extends AbstarctAbilityWidgets {
    public NileFlame(int x, int y) {
        super(x, y, 16 + (effectData.isCurseKnef() ? 1 : 0));
    }

    @Override
    public AbilityData constructAbilityData() {
        return AbilityData.builder("nile_flame").screenID(ScreenID.GLOBAL).build();
    }

    @SubscribeEvent
    public static void fluidCollide(FluidCollisionEvent event) {
        if (data.isActiveAbility("nile_flame")) {
            if (event.getEntity() instanceof Player player) {
                if (player == null) {
                    return;
                }

                if (event.getFluid().is(FluidTags.WATER) && !player.isShiftKeyDown()) {
                    event.setCanceled(true);
                }
            }
        }
    }
}
