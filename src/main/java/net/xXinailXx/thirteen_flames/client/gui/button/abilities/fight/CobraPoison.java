package net.xXinailXx.thirteen_flames.client.gui.button.abilities.fight;

import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;

public class CobraPoison extends AbstarctAbilityWidgets {
    public CobraPoison(int x, int y) {
        super(x, y, 0);
    }

    @Override
    public AbilityData constructAbilityData() {
        return AbilityData.builder("cobra_poison").screenID(ScreenID.FIGHT).maxLevel(3).requiredLevel(20).requiredScarabsForOpen(2).requiredScarabsForUpgrade(2).build();
    }
}
