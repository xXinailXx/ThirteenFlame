package net.xXinailXx.thirteen_flames.block;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.enderdragonlib.utils.statues.CustomStatueUtils;
import net.xXinailXx.enderdragonlib.utils.statues.data.StatueData;
import net.xXinailXx.thirteen_flames.block.entity.StatueBE;
import net.xXinailXx.thirteen_flames.data.IData;
import net.xXinailXx.thirteen_flames.client.gui.god_pharaoh.GodPharaohScreenMining;
import net.xXinailXx.thirteen_flames.init.BlockEntityRegistry;
import net.xXinailXx.thirteen_flames.init.BlockRegistry;
import net.xXinailXx.thirteen_flames.data.Data;
import net.xXinailXx.thirteen_flames.utils.Gods;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class StatueGodPharaoh extends CustomStatueUtils {
    public StatueGodPharaoh() {
        super(Properties.of(Material.METAL).strength(1).noOcclusion().noLootTable(), BlockRegistry.STATUE_GOD_PHARAOH_STRUCTURE.get(), Block.box(0, 0, 0, 80, 112, 80).move(-2, 0, -2));
    }

    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
        if (level.isClientSide)
            return;

        for (BlockPos pos1 : getBlockPoses(pos, false)) {
            StatueStructureBlock structureBlock = (StatueStructureBlock) this.getStructureBlock();

            level.setBlock(pos1, structureBlock.defaultBlockState(), 11);
        }

        StatueData.addStatue(new StatueData.StatueBuilder(getBlockPoses(pos, false), pos));
    }

    public void destroy(LevelAccessor accessor, BlockPos pos, BlockState state) {
        super.destroy(accessor, pos, state);

        for (BlockPos pos1 : getBlockPoses(pos, false))
            accessor.destroyBlock(pos1, false);

        StatueData.removeStatue(pos);
    }

    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity entity, ItemStack stack) {
        super.playerDestroy(level, player, pos, state, entity, stack);

        for (BlockPos pos1 : getBlockPoses(pos, false))
            level.destroyBlock(pos1, false);

        StatueData.removeStatue(pos);
    }

    @OnlyIn(Dist.CLIENT)
    public static void openPharaohScreen() {
        IData.IGuiLevelingData guiLevelingData = new Data.GuiLevelingData.Utils();
        guiLevelingData.setPlayerScreen(Minecraft.getInstance().player, false);
        Minecraft.getInstance().setScreen(new GodPharaohScreenMining());
    }

    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return BlockEntityRegistry.STATUE_GOD_PHARAOH.get().create(pos, state);
    }

    public List<BlockPos> getBlockPoses(BlockPos pos, boolean isMain) {
        Iterable<BlockPos> iterable = BlockPos.betweenClosed(pos.offset(-2, 0, -2), pos.offset(2, 6, 2));
        List<BlockPos> posList = new ArrayList<>();

        for (BlockPos blockPos : iterable) {
            if (!isMain && blockPos.equals(pos))
                continue;

            posList.add(new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ()));
        }

        return posList;
    }

    @SubscribeEvent
    public static void openMenu(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();

        if (player == null)
            return;

        Level level = event.getLevel();
        BlockState state = level.getBlockState(event.getPos());

        if (state.getBlock() instanceof StatueGodPharaoh) {
            if (level.isClientSide)
                openPharaohScreen();
        } else if (state.getBlock() instanceof StatueStructureBlock) {
            StatueData.StatueBuilder builder = StatueData.getStatue(event.getPos());

            if (builder == null)
                return;

            StatueBE be = (StatueBE) StatueData.getStatueBE(builder.mainPos());

            if (be == null || !be.isFinished() || !be.getGod().equals(Gods.GOD_PHARAOH))
                return;

            if (level.isClientSide)
                openPharaohScreen();
        }
    }
}
