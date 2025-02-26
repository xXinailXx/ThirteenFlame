package net.xXinailXx.thirteen_flames.client.gui.button.abilities.craft;

import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.IAbilityData;

@Mod.EventBusSubscriber
public class ForgeLegends extends AbstarctAbilityWidgets {
    public ForgeLegends(int x, int y) {
        super(x, y, 5);
    }

    @Override
    public AbilityData constructAbilityData() {
        return AbilityData.builder("forge_of_legends").screenID(IAbilityData.ScreenID.CRAFT).requiredLevel(60).requiredScarabsForOpen(5).build();
    }
}
