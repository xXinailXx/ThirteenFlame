package net.xXinailXx.thirteen_flames.client.gui.button.abilities.mining;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityUtils;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.ScreenID;

@Mod.EventBusSubscriber
public class GoldRush extends AbstarctAbilityWidgets {
    public GoldRush(int x, int y) {
        super( x, y, 3);
    }

    @Override
    public AbilityData constructAbilityData() {
        return AbilityData.builder("gold_rush").screenID(ScreenID.MINING).maxLevel(20).requiredLevel(15).build();
    }

    @SubscribeEvent
    public static void breakeBlock(BlockEvent.BreakEvent event) {
        BlockPos pos = event.getPos();
        Level level = event.getPlayer().getLevel();
        RandomSource random = level.getRandom();

        if (data.isActiveAbility("gold_rush")) {
            if (AbilityUtils.isRandomSuccess(level, data.getLevelAbility("gold_rush"))) {
                Block block = level.getBlockState(pos).getBlock();

                if (block.equals(Blocks.SAND)) {
                    ItemEntity itemEntity = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), Items.GOLD_NUGGET.getDefaultInstance());
                    itemEntity.setPos(pos.getX(), pos.getY(), pos.getZ());
                    level.addFreshEntity(itemEntity);
                } else if (block.equals(Blocks.GRAVEL)) {
                    ItemEntity itemEntity = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), Items.IRON_NUGGET.getDefaultInstance());
                    itemEntity.setPos(pos.getX(), pos.getY(), pos.getZ());
                    level.addFreshEntity(itemEntity);
                } else if (block.equals(Blocks.SOUL_SAND)) {
                    ItemEntity itemEntity = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), Items.NETHERITE_SCRAP.getDefaultInstance());
                    itemEntity.setPos(pos.getX(), pos.getY(), pos.getZ());
                    level.addFreshEntity(itemEntity);
                }
            }
        }
    }
}
