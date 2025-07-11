package net.xXinailXx.thirteen_flames.client.gui.button.abilities.fight;

import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.ScreenID;

public class ArrowAnubis extends AbstarctAbilityWidgets {
    public ArrowAnubis(int x, int y) {
        super(x, y, 2);
    }

    public AbilityData constructAbilityData() {
        return AbilityData.builder("arrow_anubis").screenID(ScreenID.FIGHT).maxLevel(5).requiredLevel(20).requiredScarabsForOpen(2).requiredScarabsForUpgrade(2).build();
    }
}
