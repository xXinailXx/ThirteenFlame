package net.xXinailXx.thirteen_flames.client.gui.button.abilities.global;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.IntTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.enderdragonlib.capability.PlayerCapManager;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.ScreenID;

@Mod.EventBusSubscriber
public class CoverNight extends AbstarctAbilityWidgets {
    private static int tickState = 3;
    private static Vec3 lastPos = Vec3.ZERO;

    public CoverNight(int x, int y) {
        super(x, y, 9 + (effectData.isCurseKnef(Minecraft.getInstance().player) ? 1 : 0));
    }

    public AbilityData constructAbilityData() {
        return AbilityData.builder("cover_night").screenID(ScreenID.GLOBAL).build();
    }

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;

        if (player == null || player.getLevel().isClientSide)
            return;

        if (data.isActiveAbility(player, "cover_night")) {
            Vec3 playerPos = player.position();

            if (playerPos.distanceTo(lastPos) != 0) {
                lastPos = playerPos;
                tickState = 3;

                player.setInvisible(false);
                player.removeEffect(MobEffects.REGENERATION);

                PlayerCapManager.addData(player, "tf_ability_cover_night", IntTag.valueOf(tickState));

                return;
            }

            if (tickState == 0 && !player.getLevel().isClientSide) {
                player.setInvisible(true);

                if (!player.hasEffect(MobEffects.REGENERATION))
                    player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 1, 2, true, true));
            } else {
                if (player.tickCount % 20 == 0)
                    tickState--;
            }
        } else {
            if (PlayerCapManager.getData(player, "tf_ability_cover_night") != null && ((IntTag) PlayerCapManager.getData(player, "tf_ability_cover_night")).getAsInt() == 4)
                player.setInvisible(false);

            tickState = 3;
        }

        PlayerCapManager.addData(player, "tf_ability_cover_night", IntTag.valueOf(tickState));
    }
}
