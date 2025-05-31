package net.xXinailXx.thirteen_flames.client.gui.button.abilities.craft;

import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.ScreenID;

public class CarefulHandling extends AbstarctAbilityWidgets {
    public CarefulHandling(int x, int y) {
        super(x, y, 2);
    }

    @Override
    public AbilityData constructAbilityData() {
        return AbilityData.builder("careful_handling").screenID(ScreenID.CRAFT).maxLevel(10).requiredLevel(10).build();
    }
}
