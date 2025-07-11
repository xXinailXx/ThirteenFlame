package net.xXinailXx.thirteen_flames.client.gui.button.abilities.healt;

import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.ScreenID;

public class Overcoming extends AbstarctAbilityWidgets {
    public Overcoming(int x, int y) {
        super(x, y, 0, true);
    }

    public AbilityData constructAbilityData() {
        return AbilityData.builder("overcoming").screenID(ScreenID.HEALTH).requiredLevel(30).requiredScarabsForOpen(3).build();
    }
}
