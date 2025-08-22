package net.xXinailXx.thirteen_flames.block;

import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicLevelingData;
import it.hurts.sskirillss.relics.items.relics.base.utils.LevelingUtils;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.enderdragonlib.network.packet.RemoveStatuePacket;
import net.xXinailXx.enderdragonlib.utils.statues.CustomStatueUtils;
import net.xXinailXx.enderdragonlib.utils.statues.data.StatueData;
import net.xXinailXx.thirteen_flames.config.ThirteenFlamesServerConfig;
import net.xXinailXx.thirteen_flames.init.BlockRegistry;
import net.xXinailXx.thirteen_flames.init.ItemRegistry;
import net.xXinailXx.thirteen_flames.item.BagPaintItem;
import net.xXinailXx.thirteen_flames.item.base.FlameItemSetting;
import net.xXinailXx.thirteen_flames.network.packet.FlameUpgradePacket;
import net.xXinailXx.thirteen_flames.network.packet.UpgradeStatuePacket;
import net.xXinailXx.thirteen_flames.utils.Gods;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.net.Network;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Mod.EventBusSubscriber
public abstract class StatueHandler extends CustomStatueUtils implements IAnimatable {
    @Getter
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    protected final Gods god;

    public StatueHandler(Block block, @NotNull Gods gods) {
        super(Properties.of(Material.METAL).strength(1), block, Block.box(0, 0, 0, 48, 80, 48).move(-1, 0, -1));
        this.god = gods;
    }

    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
        if (level.isClientSide)
            return;

        for (BlockPos pos1 : getBlockPoses(state.getValue(FACING), pos, false)) {
            StatueStructureBlock structureBlock = (StatueStructureBlock) this.getStructureBlock();

            level.setBlock(pos1, structureBlock.defaultBlockState(), 11);
        }

        StatueData.addStatue(level, new StatueData.StatueBuilder(getBlockPoses(state.getValue(FACING), pos, false), pos));
    }

    public void destroy(LevelAccessor accessor, BlockPos pos, BlockState state) {
        super.destroy(accessor, pos, state);

        for (BlockPos pos1 : getBlockPoses(state.getValue(FACING), pos, false))
            accessor.destroyBlock(pos1, false);

        Network.sendToServer(new RemoveStatuePacket(pos));
    }

    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity entity, ItemStack stack) {
        super.playerDestroy(level, player, pos, state, entity, stack);

        for (BlockPos pos1 : getBlockPoses(state.getValue(FACING), pos, false))
            level.destroyBlock(pos1, false);

        StatueData.removeStatue(level, pos);
    }

    public List<BlockPos> getBlockPoses(Direction direction, BlockPos pos, boolean isMain) {
        Iterable<BlockPos> iterable = BlockPos.betweenClosed(pos.offset(-1, 0, -1), pos.offset(1, 4, 1));
        List<BlockPos> posList = new ArrayList<>();

        for (BlockPos blockPos : iterable) {
            if (!isMain && blockPos.equals(pos))
                continue;

            posList.add(new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ()));
        }

        return posList;
    }

    public static boolean isUpgrade(ItemStack stack, Gods gods) {
        if (! ThirteenFlamesServerConfig.FLAME_UPGRADE_REQ_TYPE.get())
            return true;

        if (!(stack.getItem() instanceof FlameItemSetting))
            return false;

        return switch (gods) {
            case KNEF -> stack.is(ItemRegistry.BLACK_ROSE.get()) || stack.is(ItemRegistry.MOON_BOW.get());
            case SELYA -> stack.is(ItemRegistry.SUN_SELIASET.get()) || stack.is(ItemRegistry.HORN_SELIASET.get());
            case MONTU -> stack.is(ItemRegistry.HAMMER_MONTU.get()) || stack.is(ItemRegistry.GLOVES_MONTU.get());
            case RONOS -> stack.is(ItemRegistry.SWORD_RONOSA.get()) || stack.is(ItemRegistry.SHIELD_RONOSA.get());
            case HET -> stack.is(ItemRegistry.SCROLL_HET.get()) || stack.is(ItemRegistry.FLIGHT_HET.get());
            case GOD_PHARAOH -> false;
        };
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        return PlayState.CONTINUE;
    }

    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
    }

    @SubscribeEvent
    public static void useStatues(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();

        if (player == null)
            return;

        Level level = event.getLevel();
        BlockState state = level.getBlockState(event.getPos());
        ItemStack stack = player.getItemInHand(event.getHand());

        if (stack.getItem() instanceof FlameItemSetting setting) {
            RelicLevelingData data = setting.getRelicData().getLevelingData();

            if (LevelingUtils.getLevel(stack) >= data.getMaxLevel())
                return;

            if (state.getBlock() instanceof StatueHandler || state.getBlock() instanceof StatueStructureBlock) {
                Network.sendToServer(new FlameUpgradePacket(stack, event.getPos()));
                player.swing(InteractionHand.MAIN_HAND);
                event.setCanceled(true);
            }
        } else if (stack.is(ItemRegistry.STATUE_UPGRADER.get()) || (stack.getItem() instanceof BagPaintItem && !stack.is(ItemRegistry.BAG_PAINT.get()) && !stack.is(ItemRegistry.BAG_PAINT_CUP.get()))) {
            if (state.getBlock().defaultBlockState().is(BlockRegistry.STATUE_CUP_UNFINISHED.get())) {
                level.setBlock(event.getPos(), BlockRegistry.STATUE_CUP.get().defaultBlockState(), 11);
                player.swing(InteractionHand.MAIN_HAND);
                event.setCanceled(true);
            } else if (state.getBlock() instanceof StatueHandler || state.getBlock() instanceof StatueGodPharaoh || state.getBlock() instanceof StatueGodPharaohUnfinished || state.getBlock() instanceof StatueStructureBlock) {
                Network.sendToServer(new UpgradeStatuePacket(event.getPos(), stack));
                player.swing(InteractionHand.MAIN_HAND);
                event.setCanceled(true);
            }
        }

        player.swing(InteractionHand.MAIN_HAND);
    }
}
