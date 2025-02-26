package net.xXinailXx.thirteen_flames.item.base.tools;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.xXinailXx.thirteen_flames.utils.FireItemSetting;

public class TieredItemTF extends FireItemSetting {
    private final Tier tier;

    public TieredItemTF(Tier p_43308_, Item.Properties p_43309_) {
        super(p_43309_.defaultDurability(p_43308_.getUses()));
        this.tier = p_43308_;
    }

    public Tier getTier() {
        return this.tier;
    }

    public int getEnchantmentValue() {
        return this.tier.getEnchantmentValue();
    }
}
