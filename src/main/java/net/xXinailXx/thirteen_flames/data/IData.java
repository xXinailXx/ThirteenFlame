package net.xXinailXx.thirteen_flames.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.IAbilityData;

public interface IData extends INBTSerializable<CompoundTag> {
    public static interface IAbilitiesData {
        Data.AbilitiesData.Handler getAbilityHandler(String abilityName);

        void addExtraAbilitySetting(String abilityName, CompoundTag nbt);

        CompoundTag getExtraAbilitySetting(String abilityName);

        boolean isBuyAbility(String abilityName);

        void setBuyAbility(String abilityName, boolean value);

        boolean isActiveAbility(String abilityName);

        void setActiveAbility(String abilityName, boolean value);

        int getLevelAbility(String abilityName);

        void setLevelAbility(String abilityName, int amount);

        void addLevelAbility(String abilityName, int amount, int maxLevel);
    }

    public static interface IEffectData {
        int getEffectMontuAmount();

        int getEffectRonosAmount();

        int getEffectKnefAmount();

        int getEffectSelyaAmount();

        int getEffectHetAmount();

        boolean isCurseKnef();

        void setEffectMontuAmount(int amount);

        void setEffectRonosAmount(int amount);

        void setEffectKnefAmount(int amount);

        void setEffectSelyaAmount(int amount);

        void setEffectHetAmount(int amount);

        void setCurseKnef(boolean value);

        void subEffectMontuAmount(int amount);

        void subEffectRonosAmount(int amount);

        void subEffectKnefAmount(int amount);

        void subEffectSelyaAmount(int amount);

        void subEffectHetAmount(int amount);
    }

    public static interface IGuiLevelingData {
        boolean isPlayerScreen();

        int getProcentCurse();

        int getGuiMiningLevelAmount();

        int getGuiCraftLevelAmount();

        int getGuiFightLevelAmount();

        int getGuiHealthLevelAmount();

        void setPlayerScreen(boolean value);

        void setGuiMiningLevelAmount(int amount);

        void setGuiCraftLevelAmount(int amount);

        void setGuiFightLevelAmount(int amount);

        void setGuiHealthLevelAmount(int amount);

        void setProcentCurse(int amount);

        void addGuiMiningLevelAmount(int amount);

        void addGuiCraftLevelAmount(int amount);

        void addGuiFightLevelAmount(int amount);

        void addGuiHealthLevelAmount(int amount);

        void subProcentCurse(int amount);
    }

    public static interface IXpScarabsData {
        int getXpScarabSilver();

        int getXpScarabGold();

        int getXpScarabAuriteh();

        int getXpScarabLazotep();

        void setXpScarabSilver(int amount);

        void setXpScarabGold(int amount);

        void setXpScarabAuriteh(int amount);

        void setXpScarabLazotep(int amount);

        void subXpScarab(int amount);

        void subXpScarabsSilver(int amount);

        void subXpScarabsGold(int amount);

        void subXpScarabsAuriteh(int amount);

        void subXpScarabsLazotep(int amount);
    }

    public static interface IScarabsData {
        int getScarabSilver(Player player);

        int getScarabGold();

        int getScarabAuriteh();

        int getScarabLazotep();

        void setScarabSilver(Player player, int amount);

        void setScarabGold(int amount);

        void setScarabAuriteh(int amount);

        void setScarabLazotep(int amount);

        void addScarabSilver(Player player, int amount);

        void addScarabGold(int amount);

        void addScarabAuriteh(int amount);

        void addScarabLazotep(int amount);

        void resetScarabs(Player player);
    }
}