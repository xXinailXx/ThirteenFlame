package net.xXinailXx.thirteen_flames.client.gui.button.abilities.global;

import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;

public class FurySky extends AbstarctAbilityWidgets {
    public FurySky(int x, int y) {
        super(x, y, 4);
    }

    @Override
    public AbilityData constructAbilityData() {
        return AbilityData.builder("fury_sky").screenID(ScreenID.GLOBAL).build();
    }
}
