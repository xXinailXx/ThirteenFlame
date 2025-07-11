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
import net.xXinailXx.thirteen_flames.config.ThirteenFlamesConfig;
import net.xXinailXx.thirteen_flames.data.Data;
import net.xXinailXx.thirteen_flames.init.BlockRegistry;
import net.xXinailXx.thirteen_flames.init.ItemRegistry;
import net.xXinailXx.thirteen_flames.network.packet.CreativeUpdateStatuePacket;
import net.xXinailXx.thirteen_flames.network.packet.FlameUpgradePacket;
import net.xXinailXx.thirteen_flames.item.base.FlameItemSetting;
import net.xXinailXx.thirteen_flames.utils.Gods;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.net.Network;

public class StatueStructureBlock extends Block {
    public StatueStructureBlock() {
        super(Properties.of(Material.BARRIER).strength(1F).noLootTable().noOcclusion());
    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if (level.getBlockState(pos).is(BlockRegistry.STATUE_GOD_PHARAOH_STRUCTURE.get())) {
            StatueBE be = getMainBlockBE(pos);

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

            StatueBE be = getMainBlockBE(pos);

            if (be == null || !be.isFinished() || be.getTimeToUpgrade() > 0)
                return InteractionResult.FAIL;

            Network.sendToServer(new FlameUpgradePacket(stack));

            be.resetFlameUpgradeData();
        }

        return InteractionResult.SUCCESS;
    }

    public void destroy(LevelAccessor accessor, BlockPos pos, BlockState state) {
        super.destroy(accessor, pos, state);

        if (getBuilder(pos) == null) {
            accessor.destroyBlock(pos, true);
            return;
        }

        for (BlockPos pos1 : getBuilder(pos).posList())
            accessor.destroyBlock(pos1, false);

        accessor.destroyBlock(getBuilder(pos).mainPos(), true);
    }

    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity entity, ItemStack stack) {
        super.playerDestroy(level, player, pos, state, entity, stack);

        if (getBuilder(pos) == null) {
            level.destroyBlock(pos, true);
            return;
        }

        for (BlockPos pos1 : getBuilder(pos).posList()) {
            level.destroyBlock(pos1, false);
        }

        level.destroyBlock(getBuilder(pos).mainPos(), true);
    }

    @Nullable
    public Data.StatueBuilderData.StatueBuilder getBuilder(BlockPos pos) {
        return Data.StatueBuilderData.getStatue(pos);
    }

    @Nullable
    public StatueBE getMainBlockBE(BlockPos pos) {
        if (Data.StatueBuilderData.containsStatue(pos))
            return Data.StatueBuilderData.getStatueBE(Data.StatueBuilderData.getStatue(pos));

        return null;
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
