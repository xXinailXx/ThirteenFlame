package net.xXinailXx.thirteen_flames.client.gui.button.abilities.data;

import net.xXinailXx.thirteen_flames.data.IData;
import net.xXinailXx.thirteen_flames.data.Data;

public interface IAbilityData {
    Data.AbilitiesData.Handler getInfo();

    AbilityData constructAbilityData();

    default AbilityData getAbilityData() {
        if (!AbilityStorage.ABILITY.containsKey(this))
            AbilityStorage.ABILITY.put(this, constructAbilityData());

        return AbilityStorage.ABILITY.get(this);
    }

    default void setAbilityData(AbilityData data) {
        AbilityStorage.ABILITY.put(this, data);
    }

    default IAbilityData getIAbilityData() {
        return this;
    }

    default int getScreenLevel() {
        IData.IGuiLevelingData guiLevelingData = new Data.GuiLevelingData();

        return switch (getAbilityData().getScreenID()) {
            case MINING -> guiLevelingData.getGuiMiningLevelAmount();
            case CRAFT -> guiLevelingData.getGuiCraftLevelAmount();
            case FIGHT -> guiLevelingData.getGuiFightLevelAmount();
            case HEALTH -> guiLevelingData.getGuiHealthLevelAmount();
            case GLOBAL -> 1;
        };
    }

    default String getScreenId() {
        return switch (getAbilityData().getScreenID()) {
            case MINING -> "mining";
            case CRAFT -> "craft";
            case FIGHT -> "fight";
            case HEALTH -> "health";
            case GLOBAL -> "global";
        };
    }

    public enum ScreenID {
        MINING,
        CRAFT,
        FIGHT,
        HEALTH,
        GLOBAL
    }
}
