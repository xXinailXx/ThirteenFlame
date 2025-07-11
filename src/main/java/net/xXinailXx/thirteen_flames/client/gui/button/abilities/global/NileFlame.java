package net.xXinailXx.thirteen_flames.client.gui.button.abilities.global;

import it.hurts.sskirillss.relics.api.events.common.FluidCollisionEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.ScreenID;

@Mod.EventBusSubscriber
public class NileFlame extends AbstarctAbilityWidgets {
    public NileFlame(int x, int y) {
        super(x, y, 16 + (effectData.isCurseKnef(Minecraft.getInstance().player) ? 1 : 0));
    }

    public AbilityData constructAbilityData() {
        return AbilityData.builder("nile_flame").screenID(ScreenID.GLOBAL).build();
    }

    @SubscribeEvent
    public static void fluidCollide(FluidCollisionEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (player == null)
                return;

            if (data.isActiveAbility(player, "nile_flame"))
                if (event.getFluid().is(FluidTags.WATER) && !player.isShiftKeyDown())
                    event.setCanceled(true);
        }
    }
}
