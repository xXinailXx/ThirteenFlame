package net.xXinailXx.thirteen_flames.block;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.enderdragonlib.utils.statues.CustomStatueUtils;
import net.xXinailXx.thirteen_flames.block.entity.StatueGodPharaohBE;
import net.xXinailXx.thirteen_flames.data.IData;
import net.xXinailXx.thirteen_flames.client.gui.god_pharaoh.GodPharaohScreenMining;
import net.xXinailXx.thirteen_flames.init.BlockEntityRegistry;
import net.xXinailXx.thirteen_flames.init.BlockRegistry;
import net.xXinailXx.thirteen_flames.data.Data;
import net.xXinailXx.thirteen_flames.network.packet.AddStatueBuilderDataPacket;
import net.xXinailXx.thirteen_flames.utils.Gods;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.net.Network;

import java.util.ArrayList;
import java.util.List;

public class StatueGodPharaoh extends CustomStatueUtils {
    public StatueGodPharaoh() {
        super(Properties.of(Material.METAL).strength(1).noOcclusion().noLootTable(), BlockRegistry.STATUE_GOD_PHARAOH_STRUCTURE.get(), Block.box(0.0D, 0.0D, 0.0D, 80.0D, 112.0D, 80.0D).move(-2, 0, -2));
    }

    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
        if (level.isClientSide)
            return;

        for (BlockPos pos1 : getBlockPoses(pos, false)) {
            StatueStructureBlock structureBlock = (StatueStructureBlock) this.getStructureBlock();

            level.setBlock(pos1, structureBlock.defaultBlockState(), 11);
        }

        Network.sendToServer(new AddStatueBuilderDataPacket(pos, Gods.GOD_PHARAOH));
    }

    public void destroy(LevelAccessor accessor, BlockPos pos, BlockState state) {
        super.destroy(accessor, pos, state);

        Data.StatueBuilderData.removeStatue(pos);
    }

    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity entity, ItemStack stack) {
        super.playerDestroy(level, player, pos, state, entity, stack);

        Data.StatueBuilderData.removeStatue(pos);
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
}
