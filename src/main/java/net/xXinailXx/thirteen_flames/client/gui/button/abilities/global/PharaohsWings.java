package net.xXinailXx.thirteen_flames.client.gui.button.abilities.global;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.dragonworldlib.client.utils.MessageUtil;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;

@Mod.EventBusSubscriber
public class PharaohsWings extends AbstarctAbilityWidgets {
    private static int flyTime;

    static {
        flyTime = 0;
    }

    public PharaohsWings(int x, int y) {
        super(x, y, 10 + (effectData.isCurseKnef() ? 1 : 0));
    }

    @Override
    public AbilityData constructAbilityData() {
        return AbilityData.builder("pharaohs_wings").screenID(ScreenID.GLOBAL).maxLevel(5).build();
    }

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;

        if (player == null) {
            return;
        }

        if (data.isActiveAbility("pharaohs_wings")) {
            if (!player.isCreative() && !player.isSpectator()) {
                if (flyTime > 0) {
                    if (player.isOnGround() && player.getAbilities().mayfly) {
                        player.getAbilities().mayfly = false;
                        player.getAbilities().flying = false;
                        flyTime = data.getLevelAbility("pharaohs_wings") * 2;
                        return;
                    }

                    player.getAbilities().mayfly = true;

                    if (player.getLevel().getBlockState(new BlockPos(player.position()).below(2)).isAir()) {
                        if (!player.getLevel().isClientSide) {
                            if (player.tickCount % 20 == 0) {
                                flyTime--;
                            }

                            MessageUtil.displayClientMessage(player, Component.literal(String.valueOf(flyTime / 2)));
                        }
                    }
                } else {
                    player.getAbilities().mayfly = false;
                    player.getAbilities().flying = false;

                    if (player.isOnGround()) {
                        flyTime = data.getLevelAbility("pharaohs_wings") * 2;
                    }
                }
            }
        } else {
            if (!player.isCreative() && !player.isSpectator()) {
                player.getAbilities().mayfly = false;
                player.getAbilities().flying = false;
            }
        }
    }
}
