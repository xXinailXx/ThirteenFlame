package net.xXinailXx.thirteen_flames.item.flame;

import it.hurts.sskirillss.relics.client.tooltip.base.RelicStyleData;
import it.hurts.sskirillss.relics.items.relics.base.data.base.RelicData;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityData;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityEntry;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityStat;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicLevelingData;
import it.hurts.sskirillss.relics.items.relics.base.utils.AbilityUtils;
import it.hurts.sskirillss.relics.items.relics.base.utils.LevelingUtils;
import it.hurts.sskirillss.relics.utils.MathUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.entity.ShockwaveEntity;
import net.xXinailXx.thirteen_flames.item.base.tools.PickaxeItemTF;
import net.xXinailXx.thirteen_flames.init.ItemsRegistry;
import net.xXinailXx.thirteen_flames.item.base.tools.ToolTierTF;

import static it.hurts.sskirillss.relics.items.relics.base.utils.AbilityUtils.getAbilityValue;

public class MolotMontu extends PickaxeItemTF {
    public MolotMontu() {
        super(ToolTierTF.THIRTEEN_FLAMES, 3, -2.4f);
    }

    public RelicData getRelicData() {
        return RelicData.builder().abilityData(RelicAbilityData.builder().ability("ejection", RelicAbilityEntry.builder().maxLevel(15).stat("cooldown", RelicAbilityStat.builder().initialValue(56.0, 35.0).upgradeModifier(RelicAbilityStat.Operation.ADD, -2.0).formatValue((value) -> {
            return (int)MathUtils.round(value, 0);
        }).build()).stat("radius", RelicAbilityStat.builder().initialValue(6.0, 11.0).upgradeModifier(RelicAbilityStat.Operation.ADD, 1.0).formatValue((value) -> {
            return (int)MathUtils.round(value, 1);
        }).build()).build()).ability("digging", RelicAbilityEntry.builder().maxLevel(2).stat("mining", RelicAbilityStat.builder().initialValue(1.0, 1.0).upgradeModifier(RelicAbilityStat.Operation.ADD, 1.0).formatValue((value) -> {
            return (int)MathUtils.round(value, 0);
        }).build()).build()).build()).levelingData(new RelicLevelingData(200, 15, 100)).styleData(RelicStyleData.builder().build()).build();
    }

    @Override
    public InteractionResult useOn(UseOnContext use) {
        Player player = use.getPlayer();

        if (!AbilityUtils.isAbilityOnCooldown(use.getItemInHand(), "ejection")) {
            Level level = player.getCommandSenderWorld();

            ShockwaveEntity shockwave = new ShockwaveEntity(level, (int) getAbilityValue(use.getItemInHand(), "ejection", "radius"), 0);
            shockwave.setOwner(player);
            shockwave.setPos(use.getClickedPos().getX(), use.getClickedPos().getY(), use.getClickedPos().getZ());
            level.addFreshEntity(shockwave);

            LevelingUtils.addExperience(player, use.getItemInHand(), 5);

            player.getCooldowns().addCooldown(use.getItemInHand().getItem(), (int) (getAbilityValue(use.getItemInHand(), "ejection", "cooldown") * 20));
        }

        return super.useOn(use);
    }

    @Mod.EventBusSubscriber
    public class Event {
        @SubscribeEvent
        public static void onBlockDestroy(BlockEvent.BreakEvent event) {
            ItemStack stack = event.getPlayer().getMainHandItem();
            BlockPos pos = event.getPos();
            Level level = event.getPlayer().getLevel();
            double maxLevel = getAbilityValue(stack, "digging", "mining");

            if (stack.getItem() == ItemsRegistry.MOLOT_MONTU.get()) {
                switch ((int) maxLevel) {
                    case 2 -> {
                        BlockState state = level.getBlockState(pos.below());

                        if (!(state.getMaterial() == Material.BARRIER) || !(state.getMaterial() == Material.PORTAL))
                            level.destroyBlock(pos.below(), true);
                    }
                    case 3 -> {
                        BlockState stateAbove = level.getBlockState(pos.below());
                        BlockState stateBelow = level.getBlockState(pos.above());

                        if (!(stateAbove.getMaterial() == Material.BARRIER) || !(stateAbove.getMaterial() == Material.PORTAL) && !(stateBelow.getMaterial() == Material.BARRIER) || !(stateBelow.getMaterial() == Material.PORTAL)) {
                            level.destroyBlock(pos.below(), true);
                            level.destroyBlock(pos.above(), true);
                        }
                    }
                }
            }
        }
    }
}
