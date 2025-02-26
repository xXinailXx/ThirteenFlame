package net.xXinailXx.thirteen_flames.client.gui.button.abilities.fight;

import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;

public class DivineVeil extends AbstarctAbilityWidgets {
    public DivineVeil(int x, int y) {
        super(x, y, 6);
    }

    @Override
    public AbilityData constructAbilityData() {
        return AbilityData.builder("divine_veil").screenID(ScreenID.FIGHT).maxLevel(15).requiredLevel(5).build();
    }
}
