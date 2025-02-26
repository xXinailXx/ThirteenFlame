package net.xXinailXx.thirteen_flames.client.gui.button.abilities.healt;

import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;

public class SecondWind extends AbstarctAbilityWidgets {
    public SecondWind(int x, int y) {
        super(x, y, 7);
    }

    @Override
    public AbilityData constructAbilityData() {
        return AbilityData.builder("second_wind").screenID(ScreenID.HEALTH).requiredLevel(50).requiredScarabsForOpen(5).build();
    }
}
