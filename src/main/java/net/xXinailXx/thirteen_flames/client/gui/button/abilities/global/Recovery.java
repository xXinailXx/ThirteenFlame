package net.xXinailXx.thirteen_flames.client.gui.button.abilities.global;

import net.minecraft.client.Minecraft;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.ScreenID;
import net.xXinailXx.thirteen_flames.data.Data;

public class Recovery extends AbstarctAbilityWidgets {
    public Recovery(int x, int y) {
        super(x, y, 6);
    }

    public void onPress() {
        if (effectData.isCurseKnef(Minecraft.getInstance().player))
            super.onPress();
    }

    public AbilityData constructAbilityData() {
        return AbilityData.builder("recovery").screenID(ScreenID.GLOBAL).maxLevel(14).build();
    }
}
