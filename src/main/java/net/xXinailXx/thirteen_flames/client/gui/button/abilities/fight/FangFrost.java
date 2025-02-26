package net.xXinailXx.thirteen_flames.client.gui.button.abilities.fight;

import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;

public class FangFrost extends AbstarctAbilityWidgets {
    public FangFrost(int x, int y) {
        super(x, y, 5);
    }

    @Override
    public AbilityData constructAbilityData() {
        return AbilityData.builder("fang_frost").screenID(ScreenID.FIGHT).maxLevel(5).requiredLevel(20).build();
    }
}
