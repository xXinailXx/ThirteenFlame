package net.xXinailXx.thirteen_flames.item.lights;

import it.hurts.sskirillss.relics.client.tooltip.base.RelicStyleData;
import it.hurts.sskirillss.relics.items.relics.base.data.base.RelicData;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityData;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityEntry;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityStat;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicLevelingData;
import it.hurts.sskirillss.relics.items.relics.base.utils.AbilityUtils;
import it.hurts.sskirillss.relics.items.relics.base.utils.ResearchUtils;
import it.hurts.sskirillss.relics.utils.MathUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.init.ItemsRegistry;
import net.xXinailXx.thirteen_flames.item.base.tools.SwordItemTF;

import javax.annotation.Nullable;
import java.util.List;

public class KlikRonosa extends SwordItemTF {
    public KlikRonosa(Tier tier, int attackDamage, float speedDamage, Properties properties) {
        super(tier, attackDamage, speedDamage, properties);
    }

    public RelicData getRelicData() {
        return RelicData.builder().abilityData( RelicAbilityData.builder().ability("spit", RelicAbilityEntry.builder().maxLevel(5).stat("duration", RelicAbilityStat.builder().initialValue(5.0, 13.0).upgradeModifier(RelicAbilityStat.Operation.MULTIPLY_BASE, 2.0).formatValue((value) -> {
            return MathUtils.round(value, 1);
        }).build()).stat("range", RelicAbilityStat.builder().initialValue(2.51, 4.01).upgradeModifier(RelicAbilityStat.Operation.MULTIPLY_BASE, 0.30).formatValue((value) -> {
            return MathUtils.round(value, 1);
        }).build()).stat("level", RelicAbilityStat.builder().initialValue(2.0, 6.0).upgradeModifier(RelicAbilityStat.Operation.MULTIPLY_BASE, 1.0).formatValue((value) -> {
            return MathUtils.round(value, 1);
        }).build()).build()).ability("fart", RelicAbilityEntry.builder().requiredLevel(0).maxLevel(5).stat("duration", RelicAbilityStat.builder().initialValue(6.0, 18.0).upgradeModifier( RelicAbilityStat.Operation.MULTIPLY_BASE, 3.0).formatValue((value) -> {
            return MathUtils.round(value, 1);
        }).build()).stat("cooldown", RelicAbilityStat.builder().initialValue(17.0, 37.0).upgradeModifier(RelicAbilityStat.Operation.MULTIPLY_TOTAL, 5.0).formatValue((value) -> {
            return MathUtils.round(value, 1);
        }).build()).stat("radius", RelicAbilityStat.builder().initialValue(1.93, 3.53).upgradeModifier(RelicAbilityStat.Operation.ADD, 0.40).formatValue((value) -> {
            return MathUtils.round(value, 1);
        }).build()).build()).ability("anemia", RelicAbilityEntry.builder().requiredLevel(0).maxLevel(5).stat("", RelicAbilityStat.builder().initialValue(1.0, 1.0).upgradeModifier( RelicAbilityStat.Operation.ADD, 1.0).formatValue((value) -> {
            return MathUtils.round(value, 0);
        }).build()).build()).build()).levelingData(new RelicLevelingData(200, 10, 100)).styleData( RelicStyleData.builder().borders("#32a167", "#16702e").build()).build();
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity living, LivingEntity entity) {
        living.addEffect(new MobEffectInstance( MobEffects.POISON, 100), entity);
        return super.hurtEnemy( stack, living, entity );
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        if (level != null && level.isClientSide()) {
            LocalPlayer player = Minecraft.getInstance().player;
            tooltip.add( Component.literal( " " ) );
            if (ResearchUtils.isItemResearched( player, stack.getItem() )) {
                if (Screen.hasShiftDown()) {
                    tooltip.add( Component.literal( "\u00A72▶ \u00A7aСпособности:" ).withStyle( ChatFormatting.GRAY ) );
                    tooltip.add( Component.literal( "   \u00A7a◆ \u00A7eЯд Роноса \u00A7f- \u00A77Каждый взмах клинка разбрызгивает" ) );
                    tooltip.add( Component.literal( " яд по дуге перед вами, и с каждым новым ударом он" ).withStyle( ChatFormatting.GRAY ) );
                    tooltip.add( Component.literal( " все глубже проникает в тела ваших противников." ).withStyle( ChatFormatting.GRAY ) );
                    tooltip.add( Component.literal( " Улучшение повышает смертоносность испускаемого яда" ).withStyle( ChatFormatting.GRAY ) );
                    tooltip.add( Component.literal( " и радиус поражения." ).withStyle( ChatFormatting.GRAY ) );
                    tooltip.add( Component.literal( "   \u00A7a◆ \u00A7eТоксичное Облако \u00A7f- \u00A77Выпускает облако яда, многие" ) );
                    tooltip.add( Component.literal( " года отравлявшего воды Гезиры. Существа, попавшие в" ).withStyle( ChatFormatting.GRAY ) );
                    tooltip.add( Component.literal( " область поражения, получают переодический урон." ).withStyle( ChatFormatting.GRAY ) );
                    tooltip.add( Component.literal( " Улучшение снижает перезаряду и увеличивает" ).withStyle( ChatFormatting.GRAY ) );
                    tooltip.add( Component.literal( " область действия и ее длительность." ).withStyle( ChatFormatting.GRAY ) );
                    tooltip.add( Component.literal( "   \u00A7a◆ \u00A7eАнемия \u00A7f- \u00A77Вы чувствуете, как клинок отравляет саму" ) );
                    tooltip.add( Component.literal( " вашу сущность, не позволяя вам восстанавливать" ).withStyle( ChatFormatting.GRAY ) );
                    tooltip.add( Component.literal( " здоровье, но при этом не давая расстаться с" ).withStyle( ChatFormatting.GRAY ) );
                    tooltip.add( Component.literal( " артефактом. Максимальная редкость способности" ).withStyle( ChatFormatting.GRAY ) );
                    tooltip.add( Component.literal( " позволит совладать с ядом." ).withStyle( ChatFormatting.GRAY ) );
                } else {
                    tooltip.add( Component.translatable( "tooltip.relics.relic.tooltip.shift" ).withStyle( ChatFormatting.GRAY ) );
                }
            } else {
                tooltip.add( Component.translatable( "tooltip.relics.relic.tooltip.table" ).withStyle( ChatFormatting.GRAY ) );
            }
        }
    }

    @Mod.EventBusSubscriber
    public class Event {
//        @SubscribeEvent
//        public static void addEffect(TickEvent.PlayerTickEvent event) {
//            Player player = event.player;
//            for (int i = 0; i < player.getInventory().items.size(); i++) {
//                ItemStack stack = event.player.getInventory().getItem(i);
//                double levelAnemiya = AbilityUtils.getAbilityPoints(stack, "anemiya");
//
//                if (player.getInventory().getItem(i).is( ItemsRegistry.KLIK_RONOSA.get())) {
//                    if (levelAnemiya < 5) {
//                        player.addEffect(new MobEffectInstance( EffectsRegistry.ANEMIYA.get(), 200, 1, true, true));
//                    }
//                }
//            }
//        }

        @SubscribeEvent
        public static void prohibitThrowingItem(ItemTossEvent event) {
            ItemStack stack = event.getEntity().getItem();
            double levelAnemiya = AbilityUtils.getAbilityPoints(stack, "anemiya");

            if (levelAnemiya < 5) {
                if (event.getEntity().getItem().is(ItemsRegistry.KLIK_RONOSA.get())) {
                    event.getEntity().setItem(Items.AIR.getDefaultInstance());
                    event.getPlayer().addItem(new ItemStack( ItemsRegistry.KLIK_RONOSA.get()));
                }
            }
        }
    }
}
