package net.xXinailXx.thirteen_flames.item.flame;

import com.google.common.base.Suppliers;
import it.hurts.sskirillss.relics.items.relics.base.data.base.RelicData;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityData;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityEntry;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityStat;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicLevelingData;
import it.hurts.sskirillss.relics.items.relics.base.utils.AbilityUtils;
import it.hurts.sskirillss.relics.items.relics.base.utils.LevelingUtils;
import it.hurts.sskirillss.relics.items.relics.base.utils.ResearchUtils;
import it.hurts.sskirillss.relics.utils.MathUtils;
import it.hurts.sskirillss.relics.utils.NBTUtils;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
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
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.enderdragonlib.api.events.client.EntityInteractEvent;
import net.xXinailXx.enderdragonlib.capability.managers.CompoundManager;
import net.xXinailXx.enderdragonlib.client.utils.item.tooltip.ItemBorder;
import net.xXinailXx.enderdragonlib.network.packet.SpawnParticlePacket;
import net.xXinailXx.thirteen_flames.client.renderer.item.EmissiveRenderer;
import net.xXinailXx.thirteen_flames.entity.HornSeliasetEntity;
import net.xXinailXx.thirteen_flames.entity.HornWindSeliasetEntity;
import net.xXinailXx.thirteen_flames.item.base.FlameItemSetting;
import org.zeith.hammerlib.net.Network;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Mod.EventBusSubscriber
public class HornSeliaset extends FlameItemSetting {
    public RelicData getRelicData() {
        return RelicData.builder().abilityData(RelicAbilityData.builder().ability("wind", RelicAbilityEntry.builder().maxLevel(10).stat("effective", RelicAbilityStat.builder().initialValue(1, 3.5).thresholdValue(1, 6).upgradeModifier(RelicAbilityStat.Operation.ADD, 0.25).formatValue((value) -> {
            return MathUtils.round(value, 2);
        }).build()).stat("distance", RelicAbilityStat.builder().initialValue(8, 20).thresholdValue(8, 30).upgradeModifier(RelicAbilityStat.Operation.ADD, 0.5).formatValue((value) -> {
            return MathUtils.round(value, 1);
        }).build()).build()).ability("rhythm", RelicAbilityEntry.builder().maxLevel(10).stat("waves", RelicAbilityStat.builder().initialValue(2, 2).thresholdValue(2, 12).upgradeModifier(RelicAbilityStat.Operation.ADD, 1).formatValue((value) -> {
            return (int)MathUtils.round(value, 0);
        }).build()).stat("cooldown", RelicAbilityStat.builder().initialValue(72, 52).thresholdValue(5, 72).upgradeModifier(RelicAbilityStat.Operation.ADD, -3).formatValue((value) -> {
            return (int)MathUtils.round(value, 0);
        }).build()).stat("stun", RelicAbilityStat.builder().initialValue(1, 1.5).thresholdValue(1, 10).upgradeModifier(RelicAbilityStat.Operation.ADD, 0.5).formatValue((value) -> {
            return (int)MathUtils.round(value, 0);
        }).build()).build()).build()).levelingData(new RelicLevelingData(200, 15, 200)).build();
    }

    public InteractionResult useOn(UseOnContext use) {
        BlockPos pos = use.getClickedPos().above();
        Level level = use.getLevel();
        ItemStack stack = use.getItemInHand();

        HornSeliasetEntity horn = new HornSeliasetEntity(level, (int) AbilityUtils.getAbilityValue(stack, "rhythm", "waves"), (int) AbilityUtils.getAbilityValue(stack, "rhythm", "cooldown"), (int) AbilityUtils.getAbilityValue(stack, "rhythm", "stun"));
        horn.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        horn.setOwner(use.getPlayer());

        Direction direction = null;

        switch (use.getPlayer().getDirection()) {
            case NORTH -> direction = Direction.WEST;
            case SOUTH -> direction = Direction.EAST;
            case EAST -> direction = Direction.SOUTH;
            case WEST, DOWN, UP -> direction = Direction.NORTH;
        }

        horn.setYRot(direction.toYRot());

        int cooldown = NBTUtils.getInt(stack, "horn_entity_cooldown", 0);

        if (cooldown > 0) {
            horn.setCooldownTimer(cooldown);
            horn.setPhase(2);
        }

        level.addFreshEntity(horn);

        CompoundManager.add(horn.getStringUUID(), stack.save(stack.getOrCreateTag()));
        use.getPlayer().setItemInHand(use.getHand(), Items.AIR.getDefaultInstance());

        return InteractionResult.SUCCESS;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        player.startUsingItem(hand);

        return InteractionResultHolder.consume(stack);
    }

    public void onUsingTick(ItemStack stack, LivingEntity player, int count) {
        Vec3 startPos = player.getEyePosition().add(player.getLookAngle().scale(1.5));

        HornWindSeliasetEntity entity = new HornWindSeliasetEntity(player.level, (float) AbilityUtils.getAbilityValue(stack, "wind", "effective"), (float) AbilityUtils.getAbilityValue(stack, "wind", "distance"), startPos, player.isShiftKeyDown(), stack);
        entity.setOwner(player);
        entity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.75F, 0.25F, 0);
        entity.setPos(startPos);

        player.level.addFreshEntity(entity);

        RandomSource source = player.getRandom();
        Vec3 randomVec = new Vec3(Math.random() * 2 * 0.5 - 0.5, Math.random() * 2 * 0.5 - 0.5, Math.random() * 2 * 0.5 - 0.5).normalize();
        Vec3 result = (player.getLookAngle().normalize().scale(3).add(randomVec)).normalize().scale(source.nextDouble() * 0.35 + 0.35);

        Network.sendToAll(new SpawnParticlePacket(ParticleTypes.CLOUD, startPos.x, startPos.y, startPos.z, result.x * 1.5, result.y * 1.5, result.z * 1.5));
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

    public ItemBorder constructTooltipData() {
        return ItemBorder.builder()
                .backgroundTop(0x292929)
                .backgroundBottom(0x000000)
                .borderTop(0x9a9891)
                .borderBottom(0x85837f)
                .build();
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            final Supplier<EmissiveRenderer> renderer = Suppliers.memoize(EmissiveRenderer::new);

            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return this.renderer.get();
            }
        });
    }

    @SubscribeEvent
    public static void useSun(EntityInteractEvent event) {
        Player player = event.getEntity();

        if (player == null || player.getLevel().isClientSide)
            return;

        Entity entity = event.getTarget();

        if (!(entity instanceof HornSeliasetEntity horn))
            return;

        if (player.isShiftKeyDown() && horn.getPhase() != 1) {
            ItemStack stack = ItemStack.of(CompoundManager.get(horn.getStringUUID()));

            if (horn.getCooldownTimer() > 0)
                NBTUtils.setInt(stack, "horn_entity_cooldown", horn.getCooldownTimer());

            LevelingUtils.addExperience(stack, horn.getAddExp());
            horn.remove(Entity.RemovalReason.KILLED);
            player.addItem(stack);
        } else if (horn.getPhase() == 0) {
            horn.nextPhase();
        }
    }
}
