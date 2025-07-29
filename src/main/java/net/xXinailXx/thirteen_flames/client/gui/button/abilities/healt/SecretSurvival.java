package net.xXinailXx.thirteen_flames.client.gui.button.abilities.healt;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.ScreenID;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber
public class SecretSurvival extends AbstarctAbilityWidgets {
    public SecretSurvival(int x, int y) {
        super(x, y, 1);
    }

    public AbilityData constructAbilityData() {
        return AbilityData.builder("secret_survival").screenID(ScreenID.HEALTH).requiredLevel(60).requiredScarabsForOpen(5).build();
    }

    private static void eatTargetEntity(@NotNull LivingEntity target, @NotNull Player player) {
        FoodData foodData = player.getFoodData();
        float maxHealth = target.getMaxHealth();

        if (target instanceof Player)
            return;
        else if (!foodData.needsFood())
            return;

        if (maxHealth <= 20) {
            target.hurt(DamageSource.MAGIC, maxHealth);
            foodData.eat(4, 0);
        }
    }

    @SubscribeEvent
    public static void eat(PlayerInteractEvent.EntityInteractSpecific event) {
        if (!(event.getTarget() instanceof LivingEntity entity))
            return;

        Player player = event.getEntity();

        if (player == null)
            return;

        if (data.isActiveAbility(player, "secret_survival"))
            if (player.isShiftKeyDown())
                eatTargetEntity(entity, player);
    }
}
