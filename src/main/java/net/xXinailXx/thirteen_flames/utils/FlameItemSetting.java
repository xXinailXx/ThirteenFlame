package net.xXinailXx.thirteen_flames.utils;

import it.hurts.sskirillss.relics.items.relics.base.RelicItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.phys.Vec3;
import net.xXinailXx.enderdragonlib.client.utils.item.GlowItemData;
import net.xXinailXx.enderdragonlib.interfaces.IGlowItem;
import net.xXinailXx.thirteen_flames.ThirteenFlames;

import java.awt.*;

public abstract class FlameItemSetting extends RelicItem implements IGlowItem {
    public FlameItemSetting(Properties properties) {
        super(properties.tab(ThirteenFlames.FLAME_TAB));
    }

    public FlameItemSetting() {
        this(new Properties());
    }

    @Override
    public boolean isFireResistant() {
        return true;
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }

    @Override
    public Rarity getRarity(ItemStack p_41461_) {
        return Rarity.RARE;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        short maxDurability;

        switch (stack.getRarity()) {
            case COMMON -> maxDurability = -1;
            case UNCOMMON -> maxDurability = -1;
            case RARE -> maxDurability = -1;
            case EPIC -> maxDurability = -1;
            default -> throw new IncompatibleClassChangeError();
        }

        return maxDurability;
    }

    @Override
    public GlowItemData constructGlowData() {
        return new GlowItemData(new Color(255, 187, 0), new Color(255, 251, 0), new Color(162, 255, 0), new Color(111, 255, 0), 0.1F, new Vec3(0, 0.4, 0));
    }
}
