package net.xXinailXx.thirteen_flames.client.gui.button.abilities.fight;

import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.ScreenID;

public class Retribution extends AbstarctAbilityWidgets {
    public Retribution(int x, int y) {
        super(x, y, 9);
    }

    public AbilityData constructAbilityData() {
        return AbilityData.builder("retribution").screenID(ScreenID.FIGHT).maxLevel(10).requiredLevel(40).build();
    }
}
