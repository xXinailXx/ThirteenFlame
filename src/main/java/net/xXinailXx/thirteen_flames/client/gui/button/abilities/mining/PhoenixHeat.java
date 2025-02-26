package net.xXinailXx.thirteen_flames.client.gui.button.abilities.mining;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityUtils;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;

@Mod.EventBusSubscriber
public class PhoenixHeat extends AbstarctAbilityWidgets {
    public PhoenixHeat(int x, int y) {
        super( x, y, 5);
    }

    @Override
    public AbilityData constructAbilityData() {
        return AbilityData.builder("phoenix_heat").screenID(ScreenID.MINING).requiredLevel(20).requiredScarabsForOpen(5).build();
    }

    @SubscribeEvent
    public static void breakeBlock(BlockEvent.BreakEvent event) {
        BlockPos pos = event.getPos();
        Level level = event.getPlayer().getLevel();
        RandomSource random = level.getRandom();

        if (data.isActiveAbility("phoenix_heat")) {
            if (AbilityUtils.isRandomSuccess(level, 50)) {
                if (level.getBlockState( pos ).getBlock().equals( Blocks.SAND )) {
                    ItemEntity itemEntity = new ItemEntity( level, pos.getX(), pos.getY(), pos.getZ(), Items.GLASS.getDefaultInstance() );
                    itemEntity.setPos(pos.getX(), pos.getY(), pos.getZ());
                    level.addFreshEntity( itemEntity );
                }
            }
        }
    }
}
