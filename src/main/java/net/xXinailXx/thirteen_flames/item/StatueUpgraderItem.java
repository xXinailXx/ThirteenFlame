package net.xXinailXx.thirteen_flames.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.item.base.ItemSetting;
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
}
