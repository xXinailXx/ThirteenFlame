package net.xXinailXx.thirteen_flames.client.gui.button.abilities.healt;

import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.ScreenID;

public class CelestialKinship extends AbstarctAbilityWidgets {
    public CelestialKinship(int x, int y) {
        super(x, y, 5);
    }

    @Override
    public AbilityData constructAbilityData() {
        return AbilityData.builder("celestial_kinship").screenID(ScreenID.HEALTH).requiredLevel(20).requiredScarabsForOpen(2).build();
    }
}
