package net.xXinailXx.thirteen_flames.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.init.BlockRegistry;

@Mod.EventBusSubscriber
public class GodFaraonStructureBlock extends StatueStructureBlock {
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
