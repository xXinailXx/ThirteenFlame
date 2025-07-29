package net.xXinailXx.thirteen_flames.client.gui.button.abilities.craft;

import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.ScreenID;

public class ForgeLegends extends AbstarctAbilityWidgets {
    public ForgeLegends(int x, int y) {
        super(x, y, 5);
    }

    public AbilityData constructAbilityData() {
        return AbilityData.builder("forge_of_legends").screenID(ScreenID.CRAFT).requiredLevel(60).requiredScarabsForOpen(5).build();
    }
}
