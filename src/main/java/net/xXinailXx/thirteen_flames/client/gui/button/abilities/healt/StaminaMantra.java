package net.xXinailXx.thirteen_flames.client.gui.button.abilities.healt;

import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;

public class StaminaMantra extends AbstarctAbilityWidgets {
    public StaminaMantra(int x, int y) {
        super(x, y, 6);
    }

    @Override
    public AbilityData constructAbilityData() {
        return AbilityData.builder("stamina_mantra").screenID(ScreenID.HEALTH).maxLevel(993).requiredLevel(0).build();
    }
}
