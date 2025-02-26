package net.xXinailXx.thirteen_flames.client.gui.button.abilities.craft;

import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;

public class FishermanOfFisherman extends AbstarctAbilityWidgets {
    public FishermanOfFisherman(int x, int y) {
        super(x, y, 3);
    }

    @Override
    public AbilityData constructAbilityData() {
        return AbilityData.builder("fisherman_of_fisherman").screenID(ScreenID.CRAFT).requiredLevel(10).requiredScarabsForOpen(5).build();
    }
}
