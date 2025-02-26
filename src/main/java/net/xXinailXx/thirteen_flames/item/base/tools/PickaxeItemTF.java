package net.xXinailXx.thirteen_flames.item.base.tools;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;

public class PickaxeItemTF extends DiggerItemTF {
    public PickaxeItemTF(Tier tier, int attackDamage, float speedDamage, Item.Properties properties) {
        super((float)attackDamage, speedDamage, tier, BlockTags.MINEABLE_WITH_PICKAXE, properties);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, net.minecraftforge.common.ToolAction toolAction) {
        return net.minecraftforge.common.ToolActions.DEFAULT_PICKAXE_ACTIONS.contains(toolAction);
    }
}
