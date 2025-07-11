package net.xXinailXx.thirteen_flames.client.gui.button.abilities.data;

import net.minecraft.client.Minecraft;
import net.xXinailXx.thirteen_flames.data.IData;
import net.xXinailXx.thirteen_flames.data.Data;

public interface IAbilityData {
    AbilityData constructAbilityData();

    default AbilityData getAbilityData() {
        if (!AbilityStorage.ABILITIES.containsKey(this))
            AbilityStorage.ABILITIES.put(this, constructAbilityData());

        return AbilityStorage.ABILITIES.get(this);
    }

    default void setAbilityData(AbilityData data) {
        AbilityStorage.ABILITIES.put(this, data);
    }

    default IAbilityData getIAbilityData() {
        return this;
    }

    default int getScreenLevel() {
        IData.IGuiLevelingData guiLevelingData = new Data.GuiLevelingData.Utils();

        return switch (getAbilityData().getScreenID()) {
            case MINING -> guiLevelingData.getMiningLevel(Minecraft.getInstance().player);
            case CRAFT -> guiLevelingData.getCraftLevel(Minecraft.getInstance().player);
            case FIGHT -> guiLevelingData.getFightLevel(Minecraft.getInstance().player);
            case HEALTH -> guiLevelingData.getHealthLevel(Minecraft.getInstance().player);
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
}
