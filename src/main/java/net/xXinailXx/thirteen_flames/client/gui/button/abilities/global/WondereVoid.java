package net.xXinailXx.thirteen_flames.client.gui.button.abilities.global;

import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;

public class WondereVoid extends AbstarctAbilityWidgets {
    public WondereVoid(int x, int y) {
        super(x, y, 11 + (effectData.isCurseKnef() ? 1 : 0));
    }

    @Override
    public AbilityData constructAbilityData() {
        return AbilityData.builder("wondere_void").screenID(ScreenID.GLOBAL).build();
    }
}
