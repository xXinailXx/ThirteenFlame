package net.xXinailXx.thirteen_flames.item.flame;

import it.hurts.sskirillss.relics.items.relics.base.data.base.RelicData;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityData;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityEntry;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityStat;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicLevelingData;
import it.hurts.sskirillss.relics.items.relics.base.utils.AbilityUtils;
import it.hurts.sskirillss.relics.utils.MathUtils;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.item.base.tools.SwordItemTF;
import net.xXinailXx.thirteen_flames.item.base.tools.ToolTierTF;
import org.zeith.hammerlib.util.java.tuples.Tuple3;
import oshi.util.tuples.Pair;

@Mod.EventBusSubscriber
public class TravelersSword extends SwordItemTF {
    public TravelersSword() {
        super(ToolTierTF.THIRTEEN_FLAMES, 3, -2.4f);
    }

    public RelicData getRelicData() {
        return RelicData.builder().abilityData( RelicAbilityData.builder().ability("wind_wandering", RelicAbilityEntry.builder().maxLevel(5).stat("speed", RelicAbilityStat.builder().initialValue(3.61, 9.71).upgradeModifier(RelicAbilityStat.Operation.ADD, 1.0).formatValue((value) -> {
            return MathUtils.round(value, 1);
        }).build()).build()).ability("wide_step", RelicAbilityEntry.builder().maxLevel(5).stat("speed", RelicAbilityStat.builder().initialValue(1.0, 4.0).upgradeModifier(RelicAbilityStat.Operation.ADD, 0.36).formatValue((value) -> {
            return MathUtils.round( value, 1);
        }).build()).stat("climbing_height", RelicAbilityStat.builder().initialValue(3.0, 5.0).upgradeModifier( RelicAbilityStat.Operation.ADD, 1.0).formatValue((value) -> {
            return MathUtils.round(value, 1);
        }).build()).build()).build()).levelingData(new RelicLevelingData(100, 10, 100)).build();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (hand.equals(InteractionHand.MAIN_HAND)) {
            ItemStack stack = player.getMainHandItem();
            double speedValue = AbilityUtils.getAbilityValue(stack, "wind_wandering", "speed");
            Vec3 look = player.getLookAngle().normalize();
            double lookX = look.x;
            double lookY = look.y;
            double lookZ = look.z;
            if (!player.isCrouching()) {
                player.setDeltaMovement(lookX * (speedValue * 0.1), lookY  * (speedValue * 0.1), lookZ  * (speedValue * 0.1));
            }

//        Level world = player.getCommandSenderWorld();
//        Vec3 view = player.getViewVector(0);
//        Vec3 eyeVec = player.getEyePosition(0);
//        BlockHitResult ray = world.clip(new ClipContext(eyeVec, eyeVec.add(view.x * 5, view.y * 1, view.z * 1), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
//        BlockPos pos = ray.getBlockPos();
//        int x = 0;
//        int y = 0;
//        int z = 0;
//
//        for (int i = 0; i < 360; i++) {
//            if (i % 40 == 0) {
//                level.addParticle( StorytellingParticle.RONAS_FIRE_PARTICLE.get(), (pos.getX() + Math.cos(i * 2)) , (pos.getY() + Math.sin(i * 2)) + 0.5, pos.getZ() + 5.0, 0.0d, 0.0d, 0.0d);
//                if (player.getDirection() == Direction.NORTH || player.getDirection() == Direction.SOUTH) {
//
//                } else if (player.getDirection() == Direction.EAST || player.getDirection() == Direction.WEST) {
//                    level.addParticle( StorytellingParticle.RONAS_FIRE_PARTICLE.get(), lookX, lookY + Math.sin(i * 2), lookZ + Math.cos(i * 2), 0.0d, 0.0d, 0.0d);
//                }
//            }
//        }
        }
        return super.use( level, player, hand );
    }

    protected Pair<Tuple3<Float, Float, Float>, Vec3> beamSetting() {
        return new Pair<>(new Tuple3<>(1F, 1F, 1F), new Vec3(0, 0.5, 0));
    }

    //        @SubscribeEvent
//        public static void playerTick(TickEvent.PlayerTickEvent event) {
//            Player player = event.player;
//            ItemStack itemInMainHand = player.getMainHandItem();
//            ItemStack itemInOffHand = player.getOffhandItem();
//
//            double jumpLevel = AbilityUtils.getAbilityValue( ItemsRegistry.TRAVELERS_SWORD.get().getDefaultInstance(), "wide_step", "climbing_height" );
//            AttributeModifier STEP_HEIGHT_BONUS = new AttributeModifier( UUID.fromString( "4a312f09-78e0-4f3a-95c2-07ed63212472" ), ThirteenFlames.MODID + ":running_shoes_step_height", (jumpLevel * 1.5), AttributeModifier.Operation.ADDITION );
//            AttributeInstance stepHeight = player.getAttribute( ForgeMod.STEP_HEIGHT_ADDITION.get() );
//            AttributeInstance movementSpeed = player.getAttribute( Attributes.MOVEMENT_SPEED );
//            AttributeModifier speedBonus = getSpeedBonus();
//
//            if (itemInMainHand.is( ItemsRegistry.TRAVELERS_SWORD.get()) || itemInOffHand.is( ItemsRegistry.TRAVELERS_SWORD.get())) {
//                if (!movementSpeed.hasModifier( speedBonus )) {
//                    movementSpeed.addTransientModifier( speedBonus );
//                }
//                if (!stepHeight.hasModifier( STEP_HEIGHT_BONUS )) {
//                    stepHeight.addTransientModifier( STEP_HEIGHT_BONUS );
//                }
//            } else {
//                stepHeight.removeModifier( STEP_HEIGHT_BONUS );
//                movementSpeed.removeModifier( speedBonus );
//            }
//        }
}