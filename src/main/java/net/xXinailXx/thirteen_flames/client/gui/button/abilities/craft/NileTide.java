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
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.ScreenID;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class NileTide extends AbstarctAbilityWidgets {
    public NileTide(int x, int y) {
        super(x, y, 1);
    }

    @Override
    public AbilityData constructAbilityData() {
        return AbilityData.builder("nile_tide").screenID(ScreenID.CRAFT).maxLevel(20).requiredLevel(5).build();
    }

    @SubscribeEvent
    public static void extraDrop(ItemFishedEvent event) {
        if (data.isActiveAbility("nile_tide")) {
            FishingHook hook = event.getHookEntity();
            Player player = hook.getPlayerOwner();
            Level level = hook.getLevel();
            ItemStack stack = null;

            if (!level.isClientSide) {
                if (player.getMainHandItem().canPerformAction(net.minecraftforge.common.ToolActions.FISHING_ROD_CAST))
                    stack = player.getMainHandItem();
                else
                    stack = player.getOffhandItem();

                LootContext.Builder loot_builder = new LootContext.Builder(new LootContext.Builder((ServerLevel) level).getLevel()).withParameter(LootContextParams.ORIGIN, hook.position()).withParameter(LootContextParams.TOOL, stack).withParameter(LootContextParams.THIS_ENTITY, hook).withRandom(player.getRandom()).withLuck(player.getLuck());
                loot_builder.withParameter(LootContextParams.KILLER_ENTITY, player).withParameter(LootContextParams.THIS_ENTITY, hook);
                LootTable loottable = hook.level.getServer().getLootTables().get(BuiltInLootTables.FISHING);

                List<ItemStack> list = loottable.getRandomItems(loot_builder.create(LootContextParamSets.FISHING));

                RandomSource random = level.getRandom();

                List<ItemStack> extraItems = new ArrayList<>();

                for (int i = 0; i < data.getLevelAbility("nile_tide"); i++)
                    extraItems.add(list.get(random.nextInt(0, list.size())));

                for (ItemStack itemStack : extraItems) {
                    ItemEntity itementity = new ItemEntity(level, hook.getX(), hook.getY(), hook.getZ(), itemStack);
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
