package net.xXinailXx.thirteen_flames.client.gui.button.abilities.healt;

import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.ScreenID;

public class Diet extends AbstarctAbilityWidgets {
    public Diet(int x, int y) {
        super(x, y, 9);
    }

    public AbilityData constructAbilityData() {
        return AbilityData.builder("diet").screenID(ScreenID.HEALTH).maxLevel(5).requiredLevel(0).build();
    }
}
