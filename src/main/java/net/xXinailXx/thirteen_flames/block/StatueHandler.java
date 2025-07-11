package net.xXinailXx.thirteen_flames.block;

import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicLevelingData;
import it.hurts.sskirillss.relics.items.relics.base.utils.LevelingUtils;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.xXinailXx.enderdragonlib.utils.statues.CustomStatueUtils;
import net.xXinailXx.thirteen_flames.block.entity.StatueBE;
import net.xXinailXx.thirteen_flames.config.ThirteenFlamesConfig;
import net.xXinailXx.thirteen_flames.data.Data;
import net.xXinailXx.thirteen_flames.init.ItemRegistry;
import net.xXinailXx.thirteen_flames.network.packet.AddStatueBuilderDataPacket;
import net.xXinailXx.thirteen_flames.network.packet.CreativeUpdateStatuePacket;
import net.xXinailXx.thirteen_flames.network.packet.FlameUpgradePacket;
import net.xXinailXx.thirteen_flames.item.base.FlameItemSetting;
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
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public abstract class StatueHandler extends CustomStatueUtils implements IAnimatable {
    @Getter
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private final GeoItemRenderer renderer;
    protected final Gods god;

    public StatueHandler(Block block, @NotNull Gods gods, GeoItemRenderer renderer) {
        super(Properties.of(Material.METAL).strength(1), block, Block.box(0.0D, 0.0D, 0.0D, 48.0D, 80.0D, 48.0D).move(-1, 0, -1));
        this.renderer = renderer;
        this.god = gods;
    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if (level.getBlockState(pos).getBlock() instanceof StatueGodPharaoh) {
            StatueBE be = getBE(pos);

            if (be == null || !be.isFinished() || !be.getGod().equals(Gods.GOD_PHARAOH))
                return InteractionResult.FAIL;

            if (level.isClientSide)
                StatueGodPharaoh.openPharaohScreen();

            return InteractionResult.SUCCESS;
        }

        ItemStack stack = player.getItemInHand(hand);

        if (stack.getItem() instanceof FlameItemSetting setting) {
            RelicLevelingData data = setting.getRelicData().getLevelingData();

            if (LevelingUtils.getLevel(stack) >= data.getMaxLevel())
                return InteractionResult.FAIL;

            StatueBE be = getBE(pos);

            if (be == null || be.getTimeToUpgrade() > 0)
                return InteractionResult.FAIL;

            Network.sendToServer(new FlameUpgradePacket(stack));

            be.resetFlameUpgradeData();
        }

        return InteractionResult.SUCCESS;
    }

    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
        if (level.isClientSide)
            return;

        for (BlockPos pos1 : getBlockPoses(pos, false)) {
            StatueStructureBlock structureBlock = (StatueStructureBlock) this.getStructureBlock();

            level.setBlock(pos1, structureBlock.defaultBlockState(), 11);
        }

        Network.sendToServer(new AddStatueBuilderDataPacket(pos, this.god));
    }

    public void destroy(LevelAccessor accessor, BlockPos pos, BlockState state) {
        super.destroy(accessor, pos, state);

        for (BlockPos pos1 : getBlockPoses(pos, false))
            accessor.destroyBlock(pos1, false);

        Data.StatueBuilderData.removeStatue(pos);
    }

    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity entity, ItemStack stack) {
        super.playerDestroy(level, player, pos, state, entity, stack);

        for (BlockPos pos1 : getBlockPoses(pos, false))
            level.destroyBlock(pos1, false);

        Data.StatueBuilderData.removeStatue(pos);
    }

    public List<BlockPos> getBlockPoses(BlockPos pos, boolean isMain) {
        Iterable<BlockPos> iterable = BlockPos.betweenClosed(pos.offset(-1, 0, -1), pos.offset(1, 4, 1));
        List<BlockPos> posList = new ArrayList<>();

        for (BlockPos blockPos : iterable) {
            if (!isMain && blockPos.equals(pos))
                continue;

            posList.add(new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ()));
        }

        return posList;
    }

    @Nullable
    public StatueBE getBE(BlockPos pos) {
        if (Data.StatueBuilderData.containsStatue(pos)) {
            Data.StatueBuilderData.StatueBuilder builder = Data.StatueBuilderData.getStatue(pos);

            return Data.StatueBuilderData.getStatueBE(builder);
        }

        return null;
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        return PlayState.CONTINUE;
    }

    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
    }
}
