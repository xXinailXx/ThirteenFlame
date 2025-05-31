package net.xXinailXx.thirteen_flames.item.flame;

import it.hurts.sskirillss.relics.items.relics.base.data.base.RelicData;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityData;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityEntry;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityStat;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicLevelingData;
import it.hurts.sskirillss.relics.utils.MathUtils;
import it.hurts.sskirillss.relics.utils.NBTUtils;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.xXinailXx.thirteen_flames.utils.FlameItemSetting;
import org.zeith.hammerlib.util.java.tuples.Tuple3;
import oshi.util.tuples.Pair;

public class ScrollHet extends FlameItemSetting {
    public RelicData getRelicData() {
        return RelicData.builder().abilityData( RelicAbilityData.builder().ability("catalog", RelicAbilityEntry.builder().maxLevel(10).stat("modifier", RelicAbilityStat.builder().initialValue(56.0, 35.0).upgradeModifier(RelicAbilityStat.Operation.ADD, -1.0).formatValue((value) -> {
            return (int) MathUtils.round(value, 0);
        }).build()).build()).ability("techn", RelicAbilityEntry.builder().maxLevel(2).stat("level", RelicAbilityStat.builder().initialValue(1.0, 1.0).upgradeModifier(RelicAbilityStat.Operation.ADD, 1.0).formatValue((value) -> {
            return (int)MathUtils.round(value, 0);
        }).build()).build()).build()).levelingData(new RelicLevelingData(100, 10, 100)).build();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.isCrouching()) {
            if (NBTUtils.getInt(stack, "state", 0) > 7) {
                NBTUtils.setInt(stack, "state", 0);
            } else {
                NBTUtils.setInt(stack, "state", NBTUtils.getInt(stack, "state", 0) + 1);
            }

//            if (stack.is( ItemsRegistry.SCROLL_GREEN.get())) player.setItemInHand(hand, ItemsRegistry.SCROLL_ORGANGE.get().getDefaultInstance() );
//            else if (stack.is( ItemsRegistry.SCROLL_ORGANGE.get())) player.setItemInHand(hand, ItemsRegistry.SCROLL_DARK_BLUE.get().getDefaultInstance());
//            else if (stack.is( ItemsRegistry.SCROLL_DARK_BLUE.get())) player.setItemInHand(hand, ItemsRegistry.SCROLL_VERY_BLUE.get().getDefaultInstance());
//            else if (stack.is( ItemsRegistry.SCROLL_VERY_BLUE.get())) player.setItemInHand(hand, ItemsRegistry.SCROLL_BLUE.get().getDefaultInstance());
//            else if (stack.is( ItemsRegistry.SCROLL_BLUE.get())) player.setItemInHand(hand, ItemsRegistry.SCROLL_YELOW.get().getDefaultInstance());
//            else if (stack.is( ItemsRegistry.SCROLL_YELOW.get())) player.setItemInHand(hand, ItemsRegistry.SCROLL_GRAY.get().getDefaultInstance());
//            else if (stack.is( ItemsRegistry.SCROLL_GRAY.get())) player.setItemInHand(hand, ItemsRegistry.SCROLL_RED.get().getDefaultInstance());
//            else if (stack.is( ItemsRegistry.SCROLL_RED.get())) player.setItemInHand(hand, ItemsRegistry.SCROLL_GREEN.get().getDefaultInstance());
        }
        return super.use( level, player, hand );
    }

    protected Pair<Tuple3<Float, Float, Float>, Vec3> beamSetting() {
        return new Pair<>(new Tuple3<>(1F, 1F, 1F), new Vec3(0, 0.25, 0));
    }

//    @Mod.EventBusSubscriber
//    public class Events {
//        @SubscribeEvent
//        public static void playerTick(TickEvent.PlayerTickEvent event) {
//            Player player = event.player;
//            int effectValue = (int) AbilityUtils.getAbilityValue(player.getMainHandItem().getItem().getDefaultInstance() , "hatentep_techniques", "effect_level" );
//
//            if (player.getMainHandItem().is( StorytellingItems.SCROLL_GREEN.get() ) || player.getOffhandItem().is( StorytellingItems.SCROLL_GREEN.get() )) {
//                player.addEffect( new MobEffectInstance( MobEffects.LUCK, 1, (effectValue - 1), false, false, false ) );
//            } else if (player.getMainHandItem().is( StorytellingItems.SCROLL_ORGANGE.get() ) || player.getOffhandItem().is( StorytellingItems.SCROLL_ORGANGE.get() )) {
//                player.addEffect( new MobEffectInstance( AMEffectRegistry.KNOCKBACK_RESISTANCE.get(), 1, (effectValue - 1), false, false, false ) );
//            } else if (player.getMainHandItem().is( StorytellingItems.SCROLL_VERY_BLUE.get() ) || player.getOffhandItem().is( StorytellingItems.SCROLL_VERY_BLUE.get() )) {
//                player.addEffect( new MobEffectInstance( MobEffects.DAMAGE_RESISTANCE, 1, (effectValue - 1), false, false, false ) );
//            } else if (player.getMainHandItem().is( StorytellingItems.SCROLL_DARK_BLUE.get() ) || player.getOffhandItem().is( StorytellingItems.SCROLL_DARK_BLUE.get() )) {
//                player.addEffect( new MobEffectInstance( MobEffects.DOLPHINS_GRACE, 1, (effectValue - 1), false, false, false ) );
//            } else if (player.getMainHandItem().is( StorytellingItems.SCROLL_BLUE.get() ) || player.getOffhandItem().is( StorytellingItems.SCROLL_BLUE.get() )) {
//                player.addEffect( new MobEffectInstance( MobEffects.JUMP, 1, (effectValue - 1), false, false, false ) );
//            } else if (player.getMainHandItem().is( StorytellingItems.SCROLL_YELOW.get() ) || player.getOffhandItem().is( StorytellingItems.SCROLL_YELOW.get() )) {
//                player.addEffect( new MobEffectInstance( MobEffects.DIG_SPEED, 1, (effectValue - 1), false, false, false ) );
//            } else if (player.getMainHandItem().is( StorytellingItems.SCROLL_GRAY.get() ) || player.getOffhandItem().is( StorytellingItems.SCROLL_GRAY.get() )) {
//                player.addEffect( new MobEffectInstance( MobEffects.MOVEMENT_SPEED, 1, (effectValue - 1), false, false, false ) );
//            } else if (player.getMainHandItem().is( StorytellingItems.SCROLL_RED.get() ) || player.getOffhandItem().is( StorytellingItems.SCROLL_RED.get() )) {
//                player.addEffect( new MobEffectInstance( MobEffects.DAMAGE_BOOST, 1, (effectValue - 1), false, false, false ) );
//            } else if (player.getOffhandItem().is( StorytellingItems.SCROLL_GREEN.get() )) {
//                player.addEffect( new MobEffectInstance( MobEffects.LUCK, 1, (effectValue - 1), false, false, false ) );
//            } else if (player.getOffhandItem().is( StorytellingItems.SCROLL_ORGANGE.get() )) {
//                player.addEffect( new MobEffectInstance( AMEffectRegistry.KNOCKBACK_RESISTANCE.get(), 1, (effectValue - 1), false, false, false ) );
//            } else if (player.getOffhandItem().is( StorytellingItems.SCROLL_VERY_BLUE.get() )) {
//                player.addEffect( new MobEffectInstance( MobEffects.DAMAGE_RESISTANCE, 1, (effectValue - 1), false, false, false ) );
//            } else if (player.getOffhandItem().is( StorytellingItems.SCROLL_DARK_BLUE.get() )) {
//                player.addEffect( new MobEffectInstance( MobEffects.DOLPHINS_GRACE, 1, (effectValue - 1), false, false, false ) );
//            } else if (player.getOffhandItem().is( StorytellingItems.SCROLL_BLUE.get() )) {
//                player.addEffect( new MobEffectInstance( MobEffects.JUMP, 1, (effectValue - 1), false, false, false ) );
//            } else if (player.getOffhandItem().is( StorytellingItems.SCROLL_YELOW.get() )) {
//                player.addEffect( new MobEffectInstance( MobEffects.DIG_SPEED, 1, (effectValue - 1), false, false, false ) );
//            } else if (player.getOffhandItem().is( StorytellingItems.SCROLL_GRAY.get() )) {
//                player.addEffect( new MobEffectInstance( MobEffects.MOVEMENT_SPEED, 1, (effectValue - 1), false, false, false ) );
//            } else if (player.getOffhandItem().is( StorytellingItems.SCROLL_RED.get() )) {
//                player.addEffect( new MobEffectInstance( MobEffects.DAMAGE_BOOST, 1, (effectValue - 1), false, false, false ) );
//            }
//        }
//    }
}
