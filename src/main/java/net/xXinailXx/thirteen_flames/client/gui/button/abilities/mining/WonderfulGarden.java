package net.xXinailXx.thirteen_flames.client.gui.button.abilities.mining;

import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.ScreenID;

public class WonderfulGarden extends AbstarctAbilityWidgets {
    public WonderfulGarden(int x, int y) {
        super(x, y, 9);
    }

    @Override
    public AbilityData constructAbilityData() {
        return AbilityData.builder("wonderful_garden").screenID(ScreenID.MINING).requiredLevel(10).build();
    }
}
