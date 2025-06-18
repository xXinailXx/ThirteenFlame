package net.xXinailXx.thirteen_flames.block;

import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicLevelingData;
import it.hurts.sskirillss.relics.items.relics.base.utils.LevelingUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.xXinailXx.thirteen_flames.block.entity.StatueBE;
import net.xXinailXx.thirteen_flames.data.Data;
import net.xXinailXx.thirteen_flames.init.BlocksRegistry;
import net.xXinailXx.thirteen_flames.network.packet.FlameUpgradePacket;
import net.xXinailXx.thirteen_flames.utils.FlameItemSetting;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.net.Network;

public class StatueStructureBlock extends Block {
    public StatueStructureBlock() {
        super(Properties.of(Material.BARRIER).strength(1F).noLootTable().noOcclusion());
    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if (level.getBlockState(pos).is(BlocksRegistry.STATUE_GOD_PHARAOH_STRUCTURE.get()) || level.isClientSide)
            return InteractionResult.FAIL;

        ItemStack stack = player.getItemInHand(hand);

        if (!(stack.getItem() instanceof FlameItemSetting setting))
            return InteractionResult.FAIL;

        RelicLevelingData data = setting.getRelicData().getLevelingData();

        if (data.getMaxLevel() == LevelingUtils.getLevel(stack))
            return InteractionResult.FAIL;

        Network.sendToServer(new FlameUpgradePacket(pos, stack));

        return InteractionResult.SUCCESS;
    }

    public void destroy(LevelAccessor accessor, BlockPos pos, BlockState state) {
        super.destroy(accessor, pos, state);

        if (getBuilder(pos) == null)
            return;

        for (BlockPos pos1 : getBuilder(pos).posList()) {
            accessor.destroyBlock(pos1, false);
        }

        accessor.destroyBlock(getBuilder(pos).mainPos(), true);
    }

    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity entity, ItemStack stack) {
        super.playerDestroy(level, player, pos, state, entity, stack);

        if (getBuilder(pos) == null)
            return;

        for (BlockPos pos1 : getBuilder(pos).posList()) {
            level.destroyBlock(pos1, false);
        }

        level.destroyBlock(getBuilder(pos).mainPos(), true);
    }

    public Data.StatueBuilderData.StatueBuilder getBuilder(BlockPos pos) {
        for (Data.StatueBuilderData.StatueBuilder b : Data.StatueBuilderData.getStatueList()) {
            if (b.posList().contains(pos)) {
                return b;
            }
        }

        return null;
    }

    public StatueBE getMainBlockBE(BlockPos pos) {
        return Data.StatueBuilderData.getStatueBEList().get(Data.StatueBuilderData.getStatueList().indexOf(getBuilder(pos)));
    }

    public boolean propagatesSkylightDown(BlockState state, BlockGetter getter, BlockPos pos) {
        return true;
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    public float getShadeBrightness(BlockState state, BlockGetter getter, BlockPos pos) {
        return 1F;
    }
}
