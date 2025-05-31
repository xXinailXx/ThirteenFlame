package net.xXinailXx.thirteen_flames.client.gui.button.abilities.global;

import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.ScreenID;

public class Recovery extends AbstarctAbilityWidgets {
    public Recovery(int x, int y) {
        super(x, y, 6);
    }

    @Override
    public void onPress() {
        if (effectData.isCurseKnef())
            super.onPress();
    }

    @Override
    public AbilityData constructAbilityData() {
        return AbilityData.builder("recovery").screenID(ScreenID.GLOBAL).maxLevel(14).build();
    }
}
