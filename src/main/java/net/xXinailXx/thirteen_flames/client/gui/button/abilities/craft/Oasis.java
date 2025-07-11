package net.xXinailXx.thirteen_flames.client.gui.button.abilities.craft;

import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.ScreenID;

@Mod.EventBusSubscriber
public class Oasis extends AbstarctAbilityWidgets {
    public Oasis(int x, int y) {
        super(x, y, 6);
    }

    public AbilityData constructAbilityData() {
        return AbilityData.builder("oasis").screenID(ScreenID.CRAFT).requiredLevel(15).build();
    }
}
