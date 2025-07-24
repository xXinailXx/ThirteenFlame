package net.xXinailXx.thirteen_flames.item;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.enderdragonlib.utils.statues.data.StatueData;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.block.StatueStructureBlock;
import net.xXinailXx.thirteen_flames.block.entity.StatueBE;
import net.xXinailXx.thirteen_flames.init.ItemRegistry;
import net.xXinailXx.thirteen_flames.item.base.ItemSetting;
import net.xXinailXx.thirteen_flames.utils.Gods;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Mod.EventBusSubscriber
public class StatueUpgraderItem extends ItemSetting {
    public StatueUpgraderItem() {
        super(new Properties().tab(ThirteenFlames.ITEMS_TAB).stacksTo(1));
    }

    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag isAdvanced) {
        tooltip.add(Component.translatable("item." + ThirteenFlames.MODID + ".statue_upgrader.tooltip"));
    }

    public static BagPaintItem getStack(Gods gods) {
        return switch (gods) {
            case KNEF -> (BagPaintItem) ItemRegistry.BAG_PAINT_KNEF.get();
            case SELYA -> (BagPaintItem) ItemRegistry.BAG_PAINT_SELYA.get();
            case MONTU -> (BagPaintItem) ItemRegistry.BAG_PAINT_MONTU.get();
            case RONOS -> (BagPaintItem) ItemRegistry.BAG_PAINT_RONOS.get();
            case HET -> (BagPaintItem) ItemRegistry.BAG_PAINT_HET.get();
            case GOD_PHARAOH -> (BagPaintItem) ItemRegistry.BAG_PAINT_GOD_PHARAOH.get();
        };
    }

    public static void updateStatue(Level level, BlockPos pos) {
        if (level.isClientSide)
            return;

        BlockState state = level.getBlockState(pos);
        StatueData.StatueBuilder builder = StatueData.getStatue(pos);

        if (builder == null)
            return;

        StatueBE be = (StatueBE) StatueData.getStatueBE(builder.mainPos());

        if (be.getTimeToUpgrade() > 0)
            be.setTimeToUpgrade(0);
        else
            be.resetFlameUpgradeData();
    }
}
