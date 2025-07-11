package net.xXinailXx.thirteen_flames.client.gui.button.abilities.healt;

import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.ScreenID;

public class SecondWind extends AbstarctAbilityWidgets {
    public SecondWind(int x, int y) {
        super(x, y, 7, true);
    }

    public AbilityData constructAbilityData() {
        return AbilityData.builder("second_wind").screenID(ScreenID.HEALTH).requiredLevel(50).requiredScarabsForOpen(5).build();
    }
}
