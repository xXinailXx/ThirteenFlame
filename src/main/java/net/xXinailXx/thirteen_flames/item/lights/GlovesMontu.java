package net.xXinailXx.thirteen_flames.item.lights;

import it.hurts.sskirillss.relics.client.tooltip.base.RelicStyleData;
import it.hurts.sskirillss.relics.items.relics.base.data.base.RelicData;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityData;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityEntry;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityStat;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicLevelingData;
import it.hurts.sskirillss.relics.items.relics.base.utils.ResearchUtils;
import it.hurts.sskirillss.relics.utils.MathUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.utils.FireItemSetting;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GlovesMontu extends FireItemSetting {
    public GlovesMontu(Properties properties) {
        super(properties);
    }

    public RelicData getRelicData() {
        return RelicData.builder().abilityData( RelicAbilityData.builder().ability( "fixin", RelicAbilityEntry.builder().maxLevel( 10 ).stat( "effective", RelicAbilityStat.builder().initialValue( 10.0, 25.0 ).upgradeModifier( RelicAbilityStat.Operation.ADD, 1.5 ).formatValue( (value) -> {
            return MathUtils.round( value, 1 );
        } ).build() ).build() ).ability( "usin", RelicAbilityEntry.builder().requiredLevel( 0 ).maxLevel( 5 ).stat( "boost", RelicAbilityStat.builder().initialValue( 5.0, 15.0 ).upgradeModifier( RelicAbilityStat.Operation.ADD, 1.0 ).formatValue( (value) -> {
            return MathUtils.round( value, 1 );
        } ).build() ).build() ).build() ).levelingData( new RelicLevelingData( 100, 10, 100 ) ).styleData( RelicStyleData.builder().borders( "#32a167", "#16702e" ).build() ).build();
    }

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag isAdvanced) {
        if (level != null && level.isClientSide()) {
            Player player = Minecraft.getInstance().player;
            tooltip.add( Component.literal( " " ) );
            if (ResearchUtils.isItemResearched( player, stack.getItem() )) {
                if (Screen.hasShiftDown()) {
                    tooltip.add( Component.literal( "\u00A72▶ \u00A7aСпособности:" ) );
                    tooltip.add( Component.literal( "   \u00A7a◆ \u00A7eЗолотые Руки \u00A7f- \u00A77Мастерство Монту позволяет носителю" ) );
                    tooltip.add( Component.literal( " Перчаток восстанавливать повреждённое снаряжение до" ).withStyle( ChatFormatting.GRAY ) );
                    tooltip.add( Component.literal( " изначального состояния, используя кусочки божественного" ).withStyle( ChatFormatting.GRAY ) );
                    tooltip.add( Component.literal( " Ауритэха. Нажатие ПКМ по выброшенному предмету" ).withStyle( ChatFormatting.GRAY ) );
                    tooltip.add( Component.literal( " восстановит часть его прочности, расходуя 1 Ауритэховый" ).withStyle( ChatFormatting.GRAY ) );
                    tooltip.add( Component.literal( " сомородок. Улучшение увеличивает эффективность починки." ).withStyle( ChatFormatting.GRAY ) );
                    tooltip.add( Component.literal( "   \u00A7a◆ \u00A7eТерпение и труд \u00A7f- \u00A77Сила и сноровка бога трудолюбия" ) );
                    tooltip.add( Component.literal( " передаются владельцу его перчаток, увеличивая скорость" ).withStyle( ChatFormatting.GRAY ) );
                    tooltip.add( Component.literal( " использования предметов и инструментов. Улучшение" ).withStyle( ChatFormatting.GRAY ) );
                    tooltip.add( Component.literal( " повышает эффективность бонуса." ).withStyle( ChatFormatting.GRAY ) );
                } else {
                    tooltip.add( Component.translatable( "tooltip.relics.relic.tooltip.shift" ).withStyle( ChatFormatting.GRAY ) );
                }
            } else {
                tooltip.add( Component.translatable( "tooltip.relics.relic.tooltip.table" ).withStyle( ChatFormatting.GRAY ) );
            }
        }
    }

    @Override
    public void curioTick(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
//        if (livingEntity instanceof GlovesUtils utils) {
//            if (livingEntity instanceof Player player && player.isUsingItem())
////            int update = ((int)AbilityUtils.getAbilityValue(stack, "patience_and_labour", "boost") / 5);
////            for (int i = 0; i < update; i++)
////                utils.updatingUsing(stack);
//
//                for (int i = 0; i < 1; i++) {
//                    utils.updatingUsing();
//                }
//        }
//        for (int i = 0; i < 1; i++)
//            livingEntity.updatingUsingItem();
    }

    @Mod.EventBusSubscriber
    public class Event {
        public Event() {
        }

        @SubscribeEvent
        public static void useOnItemEntity(PlayerInteractEvent.EntityInteract event) {
//            if (!(event.getTarget() instanceof ItemEntity entity)) {
//                return;
//            }
//
//            ItemStack equippedCurio = EntityUtils.findEquippedCurio(event.getEntity(), (Item)StorytellingItems.GLOVES_MONTU.get());
//            if (equippedCurio.isEmpty()) {
//                return;
//            }
//
//            Player player = event.getEntity();
//            ItemStack entityItem = entity.getItem();
//            ItemStack stack = player.getMainHandItem();
//            double durabilityRepairValue = AbilityUtils.getAbilityValue(stack, "golde_hand", "effective");
//            DurabilityUtils.repair(entityItem, 10);

//            if (entity.getItem().getDamageValue() < entity.getItem().getMaxDamage()) {
//                ItemStack entityItem = entity.getItem();
//                Player player = event.getEntity();
//                ItemStack stack = player.getMainHandItem();
//                double durabilityRepairValue = AbilityUtils.getAbilityValue(stack, "golde_hand", "effective");
//                if (stack.is(StorytellingItems.AURITEH_NUGGET.get())) {
//                    DurabilityUtils.repair(entityItem, (int)durabilityRepairValue);
//                    int amount = stack.getCount();
//                    if (amount >= 1) {
//                        stack.setCount(amount - 1);
//                    }
//                }
//            }
        }
    }
}
