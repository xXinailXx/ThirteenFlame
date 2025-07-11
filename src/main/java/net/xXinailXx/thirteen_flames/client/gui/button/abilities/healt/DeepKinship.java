package net.xXinailXx.thirteen_flames.client.gui.button.abilities.healt;

import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.ScreenID;

public class DeepKinship extends AbstarctAbilityWidgets {
    public DeepKinship(int x, int y) {
        super(x, y, 2, true);
    }

    public AbilityData constructAbilityData() {
        return AbilityData.builder("deep_kinship").screenID(ScreenID.HEALTH).requiredLevel(20).requiredScarabsForOpen(2).build();
    }
}
