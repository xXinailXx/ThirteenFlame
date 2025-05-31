package net.xXinailXx.thirteen_flames.item.flame;

import it.hurts.sskirillss.relics.items.relics.base.data.base.RelicData;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityData;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityEntry;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityStat;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicLevelingData;
import it.hurts.sskirillss.relics.items.relics.base.utils.AbilityUtils;
import it.hurts.sskirillss.relics.utils.MathUtils;
import it.hurts.sskirillss.relics.utils.NBTUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.enderdragonlib.api.events.client.EntityInteractEvent;
import net.xXinailXx.enderdragonlib.capability.managers.CompoundManager;
import net.xXinailXx.thirteen_flames.entity.HornSeliasetEntity;
import net.xXinailXx.thirteen_flames.entity.HornWindSeliasetEntity;
import net.xXinailXx.thirteen_flames.utils.FlameItemSetting;
import org.zeith.hammerlib.util.java.tuples.Tuple3;
import oshi.util.tuples.Pair;

@Mod.EventBusSubscriber
public class HornSeliaset extends FlameItemSetting {
    public RelicData getRelicData() {
        return RelicData.builder().abilityData(RelicAbilityData.builder().ability("wind", RelicAbilityEntry.builder().maxLevel(10).stat("effective", RelicAbilityStat.builder().initialValue(1, 3.5).thresholdValue(1, 6).upgradeModifier(RelicAbilityStat.Operation.ADD, 0.25).formatValue((value) -> {
            return MathUtils.round(value, 0);
        }).build()).stat("distance", RelicAbilityStat.builder().initialValue(8, 20).thresholdValue(8, 30).upgradeModifier(RelicAbilityStat.Operation.ADD, 0.5).formatValue((value) -> {
            return MathUtils.round(value, 0);
        }).build()).build()).ability("rhythm", RelicAbilityEntry.builder().maxLevel(10).stat("waves", RelicAbilityStat.builder().initialValue(2, 2).thresholdValue(2, 12).upgradeModifier(RelicAbilityStat.Operation.ADD, 1).formatValue((value) -> {
            return (int)MathUtils.round(value, 0);
        }).build()).stat("cooldown", RelicAbilityStat.builder().initialValue(72, 52).thresholdValue(5, 72).upgradeModifier(RelicAbilityStat.Operation.ADD, -3).formatValue((value) -> {
            return (int)MathUtils.round(value, 0);
        }).build()).stat("stun", RelicAbilityStat.builder().initialValue(1, 1.5).thresholdValue(1, 5).upgradeModifier(RelicAbilityStat.Operation.ADD, 0.3).formatValue((value) -> {
            return MathUtils.round(value, 0);
        }).build()).build()).build()).levelingData(new RelicLevelingData(200, 15, 200)).build();
    }

    public InteractionResult useOn(UseOnContext use) {
        BlockPos pos = use.getClickedPos().above();
        Level level = use.getLevel();
        ItemStack stack = use.getItemInHand();

        HornSeliasetEntity horn = new HornSeliasetEntity(level, (int) AbilityUtils.getAbilityValue(stack, "rhythm", "waves"), (int) AbilityUtils.getAbilityValue(stack, "rhythm", "cooldown"), (float) AbilityUtils.getAbilityValue(stack, "rhythm", "stun"));
        horn.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        horn.setOwner(use.getPlayer());

        Direction direction = null;

        switch (use.getPlayer().getDirection()) {
            case NORTH -> direction = Direction.WEST;
            case SOUTH -> direction = Direction.EAST;
            case EAST -> direction = Direction.NORTH;
            case WEST -> direction = Direction.SOUTH;
            case UP -> direction = Direction.NORTH;
            case DOWN -> direction = Direction.NORTH;
        }

        horn.setYRot(direction.toYRot());

        int cooldown = NBTUtils.getInt(stack, "horn_entity_cooldown", 0);

        if (cooldown > 0) {
            horn.setCooldownTimer(cooldown);
            horn.setPhase(2);
        }

        level.addFreshEntity(horn);

        CompoundManager.add(horn.getStringUUID(), stack.save(stack.getTag()));

        System.out.println(horn.getStringUUID());

        use.getPlayer().setItemInHand(use.getHand(), Items.AIR.getDefaultInstance());

        return InteractionResult.SUCCESS;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        player.startUsingItem(hand);

        return InteractionResultHolder.consume(stack);
    }

    public void onUsingTick(ItemStack stack, LivingEntity player, int count) {
        if (count % 2 == 0) {
            Vec3 startPos = player.getEyePosition().add(player.getLookAngle().scale(1.5));

            HornWindSeliasetEntity entity = new HornWindSeliasetEntity(player.level, (float) AbilityUtils.getAbilityValue(stack, "wind", "effective"), (float) AbilityUtils.getAbilityValue(stack, "wind", "distance"), startPos, player.isShiftKeyDown());
            entity.setOwner(player);
            entity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.75F, 0.1F, 0);
            entity.setPos(startPos);

            player.level.addFreshEntity(entity);
        }
    }

    public boolean canContinueUsing(ItemStack oldStack, ItemStack newStack) {
        return true;
    }

    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.TOOT_HORN;
    }

    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slot, isSelected);

        int cooldown = NBTUtils.getInt(stack, "horn_entity_cooldown", 0);

        if (cooldown > 0)
            NBTUtils.setInt(stack, "horn_entity_cooldown", cooldown--);
    }

    protected Pair<Tuple3<Float, Float, Float>, Vec3> beamSetting() {
        return new Pair<>(new Tuple3<>(1F, 1F, 1F), new Vec3(0, 0.15, 0));
    }

    @SubscribeEvent
    public static void useSun(EntityInteractEvent event) {
        Player player = event.getEntity();

        if (player == null || player.getLevel().isClientSide)
            return;

        Entity entity = event.getTarget();

        if (!(entity instanceof HornSeliasetEntity horn))
            return;

        if (player.isShiftKeyDown() && horn.getPhase() == 0) {
            ItemStack stack = ItemStack.of(CompoundManager.get(horn.getStringUUID()));

            if (horn.getCooldownTimer() > 0)
                NBTUtils.setInt(stack, "horn_entity_cooldown", horn.getCooldownTimer());

            horn.remove(Entity.RemovalReason.KILLED);
            player.addItem(stack);
        } else if (horn.getPhase() == 0) {
            horn.nextPhase();
        }
    }
}
