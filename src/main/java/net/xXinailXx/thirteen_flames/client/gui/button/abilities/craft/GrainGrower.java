package net.xXinailXx.thirteen_flames.client.gui.button.abilities.craft;

import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;

public class GrainGrower extends AbstarctAbilityWidgets {
    public GrainGrower(int x, int y) {
        super(x, y, 7);
    }

    @Override
    public AbilityData constructAbilityData() {
        return AbilityData.builder("grain_grower").screenID(ScreenID.CRAFT).maxLevel(15).requiredLevel(25).build();
    }
}
