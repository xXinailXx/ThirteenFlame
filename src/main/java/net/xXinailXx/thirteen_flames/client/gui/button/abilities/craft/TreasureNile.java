package net.xXinailXx.thirteen_flames.client.gui.button.abilities.craft;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityUtils;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.ScreenID;

import java.util.List;

@Mod.EventBusSubscriber
public class TreasureNile extends AbstarctAbilityWidgets {
    public TreasureNile(int x, int y) {
        super(x, y, 4);
    }

    @Override
    public AbilityData constructAbilityData() {
        return AbilityData.builder("treasure_nile").screenID(ScreenID.CRAFT).maxLevel(10).requiredLevel(50).build();
    }

    @SubscribeEvent
    public static void extraDrop(ItemFishedEvent event) {
        if (data.isActiveAbility("treasure_nile")) {
            FishingHook hook = event.getHookEntity();
            Player player = hook.getPlayerOwner();
            Level level = hook.getLevel();
            ItemStack stack = null;

            if (!level.isClientSide) {
                if (AbilityUtils.isRandomSuccess(level, data.getLevelAbility("treasure_nile"))) {
                    if (player.getMainHandItem().canPerformAction(net.minecraftforge.common.ToolActions.FISHING_ROD_CAST))
                        stack = player.getMainHandItem();
                    else
                        stack = player.getOffhandItem();

                    LootContext.Builder loot_builder = new LootContext.Builder(new LootContext.Builder((ServerLevel) level).getLevel()).withParameter(LootContextParams.ORIGIN, hook.position()).withParameter(LootContextParams.TOOL, stack).withParameter(LootContextParams.THIS_ENTITY, hook).withRandom(player.getRandom()).withLuck(player.getLuck());
                    loot_builder.withParameter(LootContextParams.KILLER_ENTITY, player).withParameter(LootContextParams.THIS_ENTITY, hook);
                    LootTable loottable = hook.level.getServer().getLootTables().get(BuiltInLootTables.END_CITY_TREASURE);

                    List<ItemStack> list = loottable.getRandomItems(loot_builder.create(LootContextParamSets.CHEST));

                    RandomSource random = level.getRandom();

                    ItemStack item = list.get(random.nextInt(0, list.size()));

                    item.setCount(1);

                    ItemEntity itementity = new ItemEntity(level, hook.getX(), hook.getY(), hook.getZ(), item);
                    double d0 = player.getX() - hook.getX();
                    double d1 = player.getY() - hook.getY();
                    double d2 = player.getZ() - hook.getZ();
                    double d3 = 0.1D;
                    itementity.setDeltaMovement(d0 * 0.1D, d1 * 0.1D + Math.sqrt(Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2)) * 0.08D, d2 * 0.1D);
                    level.addFreshEntity(itementity);
                }
            }
        }
    }
}
