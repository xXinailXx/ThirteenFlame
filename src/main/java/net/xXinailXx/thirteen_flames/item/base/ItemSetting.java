package net.xXinailXx.thirteen_flames.item.base;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemSetting extends Item {
    private Component tooltip;

    public ItemSetting(Properties properties) {
        super(properties.tab(ThirteenFlames.ITEMS_TAB));
    }

    public ItemSetting(Component tooltip) {
        this(new Properties().tab(ThirteenFlames.ITEMS_TAB));

        this.tooltip = tooltip;
    }

    public ItemSetting() {
        this(new Properties());
    }

    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag isAdvanced) {
        if (this.tooltip != null)
            tooltip.add(this.tooltip);
    }

    public Rarity getRarity(ItemStack p_41461_) {
        return Rarity.COMMON;
    }
}
