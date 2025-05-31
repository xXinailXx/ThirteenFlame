package net.xXinailXx.thirteen_flames.client.gui.button.abilities.fight;

import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.ScreenID;

public class FlowingSand extends AbstarctAbilityWidgets {
    public FlowingSand(int x, int y) {
        super(x, y, 4);
    }

    @Override
    public AbilityData constructAbilityData() {
        return AbilityData.builder("flowing_sand").screenID(ScreenID.FIGHT).requiredLevel(100).build();
    }
}
