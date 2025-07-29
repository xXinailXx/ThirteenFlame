package net.xXinailXx.thirteen_flames.client.gui.button.abilities.fight;

import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.ScreenID;

public class DesertWind extends AbstarctAbilityWidgets {
    public DesertWind(int x, int y) {
        super(x, y, 13);
    }

    public AbilityData constructAbilityData() {
        return AbilityData.builder("desert_wind").screenID(ScreenID.FIGHT).maxLevel(5).requiredLevel(10).build();
    }
}
