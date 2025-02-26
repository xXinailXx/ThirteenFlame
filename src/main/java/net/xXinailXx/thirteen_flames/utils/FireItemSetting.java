package net.xXinailXx.thirteen_flames.utils;

import it.hurts.sskirillss.relics.items.relics.base.RelicItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.phys.Vec3;
import net.xXinailXx.dragonworldlib.client.utils.item.GlowItemData;
import net.xXinailXx.dragonworldlib.interfaces.IGlowItem;
import net.xXinailXx.thirteen_flames.ThirteenFlames;

import java.awt.*;

public class FireItemSetting extends RelicItem implements IGlowItem {
    public FireItemSetting(Properties properties) {
        super((new Item.Properties().tab(ThirteenFlames.FLAME_TAB)));
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
        short var10000;
        switch (stack.getRarity()) {
            case COMMON -> var10000 = -1;
            case UNCOMMON -> var10000 = -1;
            case RARE -> var10000 = -1;
            case EPIC -> var10000 = -1;
            default -> throw new IncompatibleClassChangeError();
        }
        return var10000;
    }

    @Override
    public GlowItemData constructGlowData() {
        return new GlowItemData(new Color(255, 187, 0), new Color(255, 251, 0), new Color(162, 255, 0), new Color(111, 255, 0), 0.1F, new Vec3(0, 0.4, 0));
    }
}
