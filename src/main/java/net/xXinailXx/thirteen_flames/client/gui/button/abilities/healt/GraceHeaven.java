package net.xXinailXx.thirteen_flames.client.gui.button.abilities.healt;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AirBlock;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.ScreenID;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;

@Mod.EventBusSubscriber
public class GraceHeaven extends AbstarctAbilityWidgets {
    public GraceHeaven(int x, int y) {
        super(x, y, 8);
    }

    public AbilityData constructAbilityData() {
        return AbilityData.builder("grace_heaven").screenID(ScreenID.HEALTH).requiredLevel(10).build();
    }

    @SubscribeEvent
    public static void addFood(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        Level level = player.getLevel();

        if (player == null || level == null)
            return;

        if (data.isActiveAbility(player, "grace_heaven")) {
            if (player.tickCount % 300 == 0)
                return;

            if (!level.isDay())
                return;

            Iterator iterator = BlockPos.betweenClosed(new BlockPos(player.getX(), player.getY(), player.getZ()), new BlockPos(player.getX(), 1000, player.getZ())).iterator();
            boolean isBlock = false;

            while (iterator.hasNext()) {
                if (level.getBlockState((BlockPos) iterator.next()).isAir())
                    isBlock = true;
            }

            if (isBlock) {
                FoodData foodData = player.getFoodData();
                foodData.eat(1, 0);
            }
        }
    }
}