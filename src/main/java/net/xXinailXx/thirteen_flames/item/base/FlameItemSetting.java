package net.xXinailXx.thirteen_flames.item.base;

import it.hurts.sskirillss.relics.items.relics.base.RelicItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.xXinailXx.enderdragonlib.client.utils.item.tooltip.ItemBorder;
import net.xXinailXx.enderdragonlib.interfaces.IItemTooltip;
import net.xXinailXx.thirteen_flames.ThirteenFlames;

public abstract class FlameItemSetting extends RelicItem implements IItemTooltip {
    public FlameItemSetting(Properties properties) {
        super(properties.tab(ThirteenFlames.FLAME_TAB));
    }

    public FlameItemSetting() {
        this(new Properties());
    }

    public boolean isFireResistant() {
        return true;
    }

    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }

    public Rarity getRarity(ItemStack p_41461_) {
        return Rarity.RARE;
    }

    public int getMaxDamage(ItemStack stack) {
        return -1;
    }

    public ItemBorder constructTooltipData() {
        return ItemBorder.builder().build();
    }
}
