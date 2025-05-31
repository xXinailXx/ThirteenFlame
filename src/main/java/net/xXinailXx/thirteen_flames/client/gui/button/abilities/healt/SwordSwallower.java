package net.xXinailXx.thirteen_flames.client.gui.button.abilities.healt;

import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.ScreenID;

public class SwordSwallower extends AbstarctAbilityWidgets {
    public SwordSwallower(int x, int y) {
        super(x, y, 12);
    }

    @Override
    public AbilityData constructAbilityData() {
        return AbilityData.builder("sword_swallower").screenID(ScreenID.HEALTH).requiredLevel(35).build();
    }
}
