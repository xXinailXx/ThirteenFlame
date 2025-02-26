package net.xXinailXx.thirteen_flames.item.lights;

import it.hurts.sskirillss.relics.client.tooltip.base.RelicStyleData;
import it.hurts.sskirillss.relics.items.relics.base.data.base.RelicData;
import it.hurts.sskirillss.relics.items.relics.base.data.cast.AbilityCastStage;
import it.hurts.sskirillss.relics.items.relics.base.data.cast.AbilityCastType;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityData;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityEntry;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityStat;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicLevelingData;
import it.hurts.sskirillss.relics.items.relics.base.utils.LevelingUtils;
import it.hurts.sskirillss.relics.items.relics.base.utils.ResearchUtils;
import it.hurts.sskirillss.relics.utils.MathUtils;
import it.hurts.sskirillss.relics.utils.NBTUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.entity.ShockwaveEntity;
import net.xXinailXx.thirteen_flames.item.base.tools.PickaxeItemTF;
import net.xXinailXx.thirteen_flames.init.ItemsRegistry;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

import static it.hurts.sskirillss.relics.items.relics.base.utils.AbilityUtils.getAbilityValue;

public class MolotMontu extends PickaxeItemTF {
    public MolotMontu(Tier tier, int attackDamage, float speedDamage, Properties properties) {
        super(tier, attackDamage, speedDamage, properties);
    }

    public RelicData getRelicData() {
        return RelicData.builder().abilityData(RelicAbilityData.builder().ability("ejection", RelicAbilityEntry.builder().maxLevel(15).stat("cooldown", RelicAbilityStat.builder().initialValue(56.0, 35.0).upgradeModifier(RelicAbilityStat.Operation.ADD, -2.0).formatValue((value) -> {
            return (int)MathUtils.round(value, 0);
        }).build()).stat("radius", RelicAbilityStat.builder().initialValue(6.0, 11.0).upgradeModifier(RelicAbilityStat.Operation.ADD, 1.0).formatValue((value) -> {
            return (int)MathUtils.round(value, 1);
        }).build()).build()).ability("digging", RelicAbilityEntry.builder().maxLevel(2).stat("mining", RelicAbilityStat.builder().initialValue(1.0, 1.0).upgradeModifier(RelicAbilityStat.Operation.ADD, 1.0).formatValue((value) -> {
            return (int)MathUtils.round(value, 0);
        }).build()).build()).build()).levelingData(new RelicLevelingData(200, 15, 100)).styleData(RelicStyleData.builder().borders("#40D42F", "#35D922").build()).build();
    }

    @Override
    public InteractionResult useOn(UseOnContext use) {
        Player player = use.getPlayer();
        Level level = player.getCommandSenderWorld();

        ShockwaveEntity shockwave = new ShockwaveEntity(level, (int) getAbilityValue(use.getItemInHand(), "ejection", "radius"), 3);
        shockwave.setOwner(player);
        shockwave.setPos(use.getClickedPos().getX(), use.getClickedPos().getY(), use.getClickedPos().getZ());
        level.addFreshEntity(shockwave);

        LevelingUtils.addExperience(player, use.getItemInHand(), 5);
//
//        player.getCooldowns().addCooldown(use.getItemInHand().getItem(), (int)(getAbilityValue(use.getItemInHand(), "ejection", "cooldown") * 20));

        return super.useOn( use );
    }

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag isAdvanced) {
        if (level != null && level.isClientSide()) {
            LocalPlayer player = Minecraft.getInstance().player;
            tooltip.add(Component.literal(" "));
            if (ResearchUtils.isItemResearched(player, stack.getItem())) {
                if (Screen.hasShiftDown()) {
                    tooltip.add(Component.translatable("tooltip.st_thirteen_lights.molot_montu.tooltip"));
                } else {
                    tooltip.add(Component.translatable("tooltip.relics.relic.tooltip.shift").withStyle(ChatFormatting.GRAY));
                }
            } else {
                tooltip.add(Component.translatable("tooltip.relics.relic.tooltip.table").withStyle(ChatFormatting.GRAY));
            }
        }
    }

    @Mod.EventBusSubscriber
    public class Event {
        @SubscribeEvent
        public static void onBlockDestroy(BlockEvent.BreakEvent event) {
            ItemStack stack = event.getPlayer().getMainHandItem();
            BlockPos pos = event.getPos();
            Level level = event.getPlayer().getLevel();
            double maxLevel = getAbilityValue( stack, "digging", "mining" );
            if (stack.getItem() == ItemsRegistry.MOLOT_MONTU.get()) {
                switch ((int) maxLevel) {
                    case 2 -> {
                        BlockState state = level.getBlockState( pos.below() );
                        if (!(state.getMaterial() == Material.BARRIER) || !(state.getMaterial() == Material.PORTAL)) {
                            level.destroyBlock( pos.below(), true );
                        }
                    }
                    case 3 -> {
                        BlockState stateAbove = level.getBlockState( pos.below() );
                        BlockState stateBelow = level.getBlockState( pos.above() );
                        if (!(stateAbove.getMaterial() == Material.BARRIER) || !(stateAbove.getMaterial() == Material.PORTAL) && !(stateBelow.getMaterial() == Material.BARRIER) || !(stateBelow.getMaterial() == Material.PORTAL)) {
                            level.destroyBlock( pos.below(), true );
                            level.destroyBlock( pos.above(), true );
                        }
                    }
                }
            }
        }
    }
}
