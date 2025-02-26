package net.xXinailXx.thirteen_flames.client.gui.button.abilities.mining;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityUtils;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;

@Mod.EventBusSubscriber
public class GoldEltdorado extends AbstarctAbilityWidgets {
    public GoldEltdorado(int x, int y) {
        super(x, y, 2);
    }

    @Override
    public AbilityData constructAbilityData() {
        return AbilityData.builder("gold_eltdorado").screenID(ScreenID.MINING).maxLevel(10).requiredLevel(10).build();
    }

    @SubscribeEvent
    public static void breakeBlock(BlockEvent.BreakEvent event) {
        BlockPos pos = event.getPos();
        Level level = event.getPlayer().getLevel();

        if (data.isActiveAbility("gold_eltdorado")) {
            if (AbilityUtils.isRandomSuccess(level, data.getLevelAbility("gold_eltdorado"))) {
                ItemEntity itemEntity = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), Items.GOLD_NUGGET.getDefaultInstance());
                itemEntity.setPos(pos.getX(), pos.getY(), pos.getZ());
                level.addFreshEntity(itemEntity);
            }
        }
    }
}
