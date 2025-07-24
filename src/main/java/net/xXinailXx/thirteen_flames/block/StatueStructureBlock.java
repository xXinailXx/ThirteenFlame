package net.xXinailXx.thirteen_flames.block;

import net.minecraft.core.BlockPos;
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
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.enderdragonlib.utils.statues.data.StatueData;
import net.xXinailXx.thirteen_flames.block.entity.StatueBE;
import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber
public class StatueStructureBlock extends Block {
    public StatueStructureBlock() {
        super(Properties.of(Material.BARRIER).strength(1F).noLootTable().noOcclusion());
    }

    public void destroy(LevelAccessor accessor, BlockPos pos, BlockState state) {
        super.destroy(accessor, pos, state);

        if (StatueData.getStatue(pos) == null) {
            accessor.destroyBlock(pos, true);
            return;
        }

        for (BlockPos pos1 : StatueData.getStatue(pos).posList())
            accessor.destroyBlock(pos1, false);

        accessor.destroyBlock(StatueData.getStatue(pos).mainPos(), true);
    }

    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity entity, ItemStack stack) {
        super.playerDestroy(level, player, pos, state, entity, stack);

        if (StatueData.getStatue(pos) == null) {
            level.destroyBlock(pos, true);
            return;
        }

        for (BlockPos pos1 : StatueData.getStatue(pos).posList()) {
            level.destroyBlock(pos1, false);
        }

        level.destroyBlock(StatueData.getStatue(pos).mainPos(), true);
    }

    @Nullable
    public StatueBE getMainBlockBE(BlockPos pos) {
        StatueData.StatueBuilder builder = StatueData.getStatue(pos);

        if (builder == null)
            return null;

        return (StatueBE) StatueData.getStatueBE(builder.mainPos());
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
