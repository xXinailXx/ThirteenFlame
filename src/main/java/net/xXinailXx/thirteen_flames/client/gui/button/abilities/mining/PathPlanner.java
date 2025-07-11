package net.xXinailXx.thirteen_flames.client.gui.button.abilities.mining;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.*;

@Mod.EventBusSubscriber
public class PathPlanner extends AbstarctAbilityWidgets {
    public PathPlanner(int x, int y) {
        super( x, y, 8);
    }

    public AbilityData constructAbilityData() {
        return AbilityData.builder("path_planner").screenID(ScreenID.MINING).requiredLevel(20).requiredScarabsForOpen(3).build();
    }

    @SubscribeEvent
    public static void breakeBlock(BlockEvent.BreakEvent event) {
        ItemStack itemInHand = event.getPlayer().getMainHandItem();
        BlockPos pos = event.getPos();
        Level level = event.getPlayer().getLevel();
        Player player = event.getPlayer();

        if (!player.isCreative()) {
            if (itemInHand.getItem() instanceof PickaxeItem) {
                if (data.isActiveAbility(player, "path_planner")) {
                    boolean error = false;

                    if (data.isActiveAbility(player, "magomed_walks"))
                        error = true;

                    if (!error) {
                        if (!player.isShiftKeyDown()) {
                            level.destroyBlock(pos.below(), true);
                            level.destroyBlock(pos.above(), true);
                        }
                    }
                }
            }
        }
    }
}
