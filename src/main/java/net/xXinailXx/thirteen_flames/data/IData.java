package net.xXinailXx.thirteen_flames.data;

import net.minecraft.world.entity.player.Player;

public interface IData {
    public static interface IAbilitiesData {
        boolean isLockAbility(Player player, String ability);

        void setLockAbility(Player player, String ability, boolean value);

        boolean isBuyAbility(Player player, String ability);

        void setBuyAbility(Player player, String ability, boolean value);

        boolean isActiveAbility(Player player, String ability);

        void setActiveAbility(Player player, String ability, boolean value);

        int getLevelAbility(Player player, String ability);

        void setLevelAbility(Player player, String ability, int amount);

        void addLevelAbility(Player player, String ability, int amount);
    }

    public static interface IEffectData {
        int getKnef(Player player);

        int getSelya(Player player);

        int getMontu(Player player);

        int getRonos(Player player);

        int getHet(Player player);

        boolean isCurseKnef(Player player);

        void setKnef(Player player, int amount);

        void setSelya(Player player, int amount);

        void setMontu(Player player, int amount);

        void setRonos(Player player, int amount);

        void setHet(Player player, int amount);

        void setCurseKnef(Player player, boolean value);

        void subKnef(Player player, int amount);

        void subSelya(Player player, int amount);

        void subMontu(Player player, int amount);

        void subRonos(Player player, int amount);

        void subHet(Player player, int amount);
    }

    public static interface IGuiLevelingData {
        boolean isPlayerScreen(Player player);

        int getProcentCurse(Player player);

        int getMiningLevel(Player player);

        int getCraftLevel(Player player);

        int getFightLevel(Player player);

        int getHealthLevel(Player player);

        void setPlayerScreen(Player player, boolean value);

        void setMiningLevel(Player player, int amount);

        void setCraftLevel(Player player, int amount);

        void setFightLevel(Player player, int amount);

        void setHealthLevel(Player player, int amount);

        void setProcentCurse(Player player, int amount);

        void addMiningLevel(Player player, int amount);

        void addCraftLevel(Player player, int amount);

        void addFightLevel(Player player, int amount);

        void addHealthLevel(Player player, int amount);
    }

    public static interface IXpScarabsData {
        int getXpScarabSilver(Player player);

        int getXpScarabGold(Player player);

        int getXpScarabAuriteh(Player player);

        int getXpScarabLazotep(Player player);

        void setXpScarabSilver(Player player, int amount);

        void setXpScarabGold(Player player, int amount);

        void setXpScarabAuriteh(Player player, int amount);

        void setXpScarabLazotep(Player player, int amount);

        void subXpScarab(Player player, int amount);

        void subXpScarabsSilver(Player player, int amount);

        void subXpScarabsGold(Player player, int amount);

        void subXpScarabsAuriteh(Player player, int amount);

        void subXpScarabsLazotep(Player player, int amount);
    }

    public static interface IScarabsData {
        int getScarabSilver(Player player);

        int getScarabGold(Player player);

        int getScarabAuriteh(Player player);

        int getScarabLazotep(Player player);

        void setScarabSilver(Player player, int amount);

        void setScarabGold(Player player, int amount);

        void setScarabAuriteh(Player player, int amount);

        void setScarabLazotep(Player player, int amount);

        void addScarabSilver(Player player, int amount);

        void addScarabGold(Player player, int amount);

        void addScarabAuriteh(Player player, int amount);

        void addScarabLazotep(Player player, int amount);

        void resetScarabs(Player player);
    }
}