package net.xXinailXx.thirteen_flames.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.xXinailXx.enderdragonlib.utils.statues.CustomStatueUtils;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.block.StatueCup;
import net.xXinailXx.thirteen_flames.block.StatueStructureBlock;

public class StatueBlockItem extends BlockItem {
    public StatueBlockItem(Block block, Properties properties) {
        super(block, block instanceof CustomStatueUtils || block instanceof StatueStructureBlock || block instanceof StatueCup ? properties.rarity(Rarity.RARE).tab(ThirteenFlames.STATUES_TAB) : properties.tab(ThirteenFlames.ITEMS_TAB));
    }
}
