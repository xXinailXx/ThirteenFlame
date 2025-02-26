package net.xXinailXx.thirteen_flames.block;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.dragonworldlib.utils.statues.StatueSize5x5Util;
import net.xXinailXx.thirteen_flames.data.IData;
import net.xXinailXx.thirteen_flames.client.gui.GodFaraon.GodFaraonScreenMining;
import net.xXinailXx.thirteen_flames.init.BlockEntityRegistry;
import net.xXinailXx.thirteen_flames.init.BlockRegistry;
import net.xXinailXx.thirteen_flames.data.Data;
import org.jetbrains.annotations.Nullable;

public class GodFaraon extends StatueSize5x5Util {
    public GodFaraon() {
    }

    @OnlyIn(Dist.CLIENT)
    public static void openFaraonScreen() {
        if (Minecraft.getInstance().player == null) {
            return;
        }

        IData.IGuiLevelingData guiLevelingData = new Data.GuiLevelingData();
        guiLevelingData.setPlayerScreen(false);
        Minecraft.getInstance().setScreen(new GodFaraonScreenMining());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return BlockEntityRegistry.GOD_FARAON_BLOCK_ENTITY.get().create(pos, state);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState blockState, boolean drop) {
        BlockPos mainPos = this.getMainPos(state, pos);
        getBlocksList(mainPos, true).forEach(blockPos -> level.setBlock(blockPos, BlockRegistry.GOD_FARAON_STRUCTURE_BLOCK.get().defaultBlockState(), 5));
        getBlocksList(mainPos, true).forEach(blockPos -> level.destroyBlock(blockPos, drop));
        getBlocksList(mainPos, true).forEach(blockPos -> level.setBlock(blockPos, BlockRegistry.GOD_FARAON_STRUCTURE_BLOCK.get().defaultBlockState(), 5));
    }

    @Mod.EventBusSubscriber
    public static class Event {
        public Event() {
        }
        @SubscribeEvent
        public static void onRightClick(PlayerInteractEvent.RightClickBlock event) {
            InteractionHand hand = event.getHand();
            if (hand == InteractionHand.MAIN_HAND) {
                Level level = event.getLevel();
                BlockPos pos = event.getPos();
                BlockState state = event.getLevel().getBlockState(pos);
                if (state.is( BlockRegistry.GOD_FARAON_STRUCTURE_BLOCK.get()) || state.is( BlockRegistry.GOD_FARAON_BLOCK.get())) {
                    if (level.isClientSide) {
                        GodFaraon.openFaraonScreen();
                    }
                }
            }
        }
    }
}
