package net.xXinailXx.thirteen_flames.client.gui.button.abilities.global;

import net.minecraft.client.Minecraft;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.ScreenID;

public class WondereVoid extends AbstarctAbilityWidgets {
    public WondereVoid(int x, int y) {
        super(x, y, 11 + (effectData.isCurseKnef(Minecraft.getInstance().player) ? 1 : 0));
    }

    public AbilityData constructAbilityData() {
        return AbilityData.builder("wondere_void").screenID(ScreenID.GLOBAL).build();
    }
}
