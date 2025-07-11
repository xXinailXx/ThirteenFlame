package net.xXinailXx.thirteen_flames.client.gui.button.abilities.mining;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityStorage;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityUtils;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.ScreenID;

@Mod.EventBusSubscriber
public class MinesSolomon extends AbstarctAbilityWidgets {
    public MinesSolomon(int x, int y) {
        super(x, y, 0);
    }

    public AbilityData constructAbilityData() {
        return AbilityData.builder("mines_solomon").screenID(ScreenID.MINING).requiredLevel(80).requiredScarabsForOpen(5).build();
    }

    @SubscribeEvent
    public static void breakeBlock(BlockEvent.BreakEvent event) {
        ItemStack itemInHand = event.getPlayer().getMainHandItem();
        BlockPos pos = event.getPos();
        Level level = event.getPlayer().getLevel();
        Player player = event.getPlayer();

        if (!player.isCreative()) {
            if (itemInHand.getItem() instanceof ShovelItem) {
                if (data.isActiveAbility(player, "mines_solomon")) {
                    boolean error = false;

                    if (data.isActiveAbility(player, "excavations"))
                        error = true;

                    if (!error) {
                        if (!player.isShiftKeyDown())
                            AbilityUtils.breakBlock(player, level, pos, 5);
                    }
                }
            }
        }
    }
}
