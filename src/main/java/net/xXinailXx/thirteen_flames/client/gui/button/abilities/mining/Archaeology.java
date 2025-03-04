package net.xXinailXx.thirteen_flames.client.gui.button.abilities.mining;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;

@Mod.EventBusSubscriber
public class Archaeology extends AbstarctAbilityWidgets {
    public Archaeology(int x, int y) {
        super(x, y, 1);
    }

    @Override
    public AbilityData constructAbilityData() {
        return AbilityData.builder("archaeology").screenID(ScreenID.MINING).maxLevel(5).requiredLevel(100).requiredScarabsForOpen(5).requiredScarabsForUpgrade(5).build();
    }

    @SubscribeEvent
    public static void breakeBlock(BlockEvent.BreakEvent event) {
        ItemStack imenInHand = event.getPlayer().getMainHandItem();
        Player player = event.getPlayer();

        if (!player.isCreative()) {
            if (imenInHand.getItem() instanceof ShovelItem) {
                if (data.isActiveAbility("archaeology"))
                    event.setExpToDrop(event.getExpToDrop() + data.getLevelAbility("archaeology"));
            }
        }
    }
}
