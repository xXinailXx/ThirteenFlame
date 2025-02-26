package net.xXinailXx.thirteen_flames.client.gui.button.abilities.global;

import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;

public class GiftGodFaraon extends AbstarctAbilityWidgets {
    public GiftGodFaraon(int x, int y) {
        super(x, y, 13 + (effectData.isCurseKnef() ? 1 : 0));
    }

    @Override
    public AbilityData constructAbilityData() {
        return AbilityData.builder("gift_god_faraon").screenID(ScreenID.GLOBAL).build();
    }
}
