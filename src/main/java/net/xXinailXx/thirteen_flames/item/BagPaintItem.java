package net.xXinailXx.thirteen_flames.item;

import lombok.Getter;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.item.base.ItemSetting;
import net.xXinailXx.thirteen_flames.utils.Gods;

@Getter
public class BagPaintItem extends ItemSetting {
    private final Gods god;

    public BagPaintItem(Gods god) {
        super(new Properties().tab(ThirteenFlames.ITEMS_TAB).stacksTo(1));
        this.god = god;
    }
}
