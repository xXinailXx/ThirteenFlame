package net.xXinailXx.thirteen_flames.item.flame;

import it.hurts.sskirillss.relics.items.relics.base.data.base.RelicData;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityData;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityEntry;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityStat;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicLevelingData;
import it.hurts.sskirillss.relics.items.relics.base.utils.AbilityUtils;
import it.hurts.sskirillss.relics.items.relics.base.utils.LevelingUtils;
import it.hurts.sskirillss.relics.utils.MathUtils;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.xXinailXx.thirteen_flames.init.ItemRegistry;
import net.xXinailXx.thirteen_flames.utils.FlameItemSetting;
import org.zeith.hammerlib.util.java.tuples.Tuple3;
import oshi.util.tuples.Pair;

public class FlightHet extends FlameItemSetting {
    public RelicData getRelicData() {
        return RelicData.builder().abilityData(RelicAbilityData.builder().ability("mapmaster", RelicAbilityEntry.builder().maxLevel(10).stat("price", RelicAbilityStat.builder().initialValue(25, 15).thresholdValue(1, 25).upgradeModifier(RelicAbilityStat.Operation.ADD, -1).formatValue((value) -> {
            return (int)MathUtils.round(value, 0);
        }).build()).build()).build()).levelingData(new RelicLevelingData(100, 10, 100)).build();
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack mainStack = player.getMainHandItem();
        ItemStack offStack = player.getOffhandItem();

        if (mainStack.is(ItemRegistry.FLIGHT_HET.get()) && offStack.is(Items.PAPER)) {
            int price = (int) AbilityUtils.getAbilityValue(mainStack, "mapmaster", "price");

            if (player.experienceLevel >= price) {
                player.getOffhandItem().shrink(1);
                ItemStack newStack = Items.MAP.getDefaultInstance();

                if (player.getOffhandItem().isEmpty())
                    player.setItemSlot(EquipmentSlot.OFFHAND, newStack);

                if (player.addItem(newStack))
                    player.drop(newStack, false);
                else
                    player.addItem(Items.MAP.getDefaultInstance());

                if (!player.isCreative()) {
                    player.giveExperienceLevels(-price);
                }

                System.out.println(price);

                LevelingUtils.addExperience(mainStack, 2);
            }
        }

        return super.use(level, player, hand);
    }

    protected Pair<Tuple3<Float, Float, Float>, Vec3> beamSetting() {
        return new Pair<>(new Tuple3<>(1F, 1F, 1F), new Vec3(0, 0.5, 0));
    }
}
