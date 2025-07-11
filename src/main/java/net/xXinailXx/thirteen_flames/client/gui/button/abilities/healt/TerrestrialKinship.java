package net.xXinailXx.thirteen_flames.client.gui.button.abilities.healt;

import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.ScreenID;

public class TerrestrialKinship extends AbstarctAbilityWidgets {
    public TerrestrialKinship(int x, int y) {
        super(x, y, 11, true);
    }

    public AbilityData constructAbilityData() {
        return AbilityData.builder("terrestrial_kinship").screenID(ScreenID.HEALTH).requiredLevel(20).requiredScarabsForOpen(2).build();
    }
}
