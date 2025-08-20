package net.xXinailXx.thirteen_flames.data;

import net.minecraft.world.entity.player.Player;

public interface IStaminaData {
    int getMaxStamina(Player player);

    void setMaxStamina(Player player, int maxStamina);

    int getStamina(Player player);

    void setStamina(Player player, int stamina);

    int getRegenCooldown(Player player);

    void setRegenCooldown(Player player, int regenCooldown);

    int getShakeTime(Player player);

    void setShakeTime(Player player, int shakeTime);

    void addStamina(Player player, int stamina);

    void addStaminaReqAbility(Player player, int stamina);

    void addMaxStamina(Player player, int stamina);

    void addRegenCooldown(Player player, int cooldown);

    void addShakeTime(Player player, int time);

    boolean isStaminaEmpty(Player player);

    boolean isStaminaFull(Player player);
}
