package net.xXinailXx.thirteen_flames.item.base.tools;

import net.minecraft.world.item.Tier;
import net.xXinailXx.thirteen_flames.utils.FlameItemSetting;

public class TieredItemTF extends FlameItemSetting {
    private final Tier tier;

    public TieredItemTF(Tier tier) {
        super(new Properties().defaultDurability(tier.getUses()));
        this.tier = tier;
    }

    public Tier getTier() {
        return this.tier;
    }

    public int getEnchantmentValue() {
        return this.tier.getEnchantmentValue();
    }
}
