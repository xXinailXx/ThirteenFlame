package net.xXinailXx.thirteen_flames.block;

import it.hurts.sskirillss.relics.utils.DurabilityUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.data.Data;
import net.xXinailXx.thirteen_flames.init.BlocksRegistry;
import net.xXinailXx.thirteen_flames.init.ItemsRegistry;

public class StatueCup extends Block {
    public StatueCup() {
        super(Properties.of(Material.METAL).strength(1).noOcclusion());
    }

    @Mod.EventBusSubscriber
    public static class Events {
        @SubscribeEvent
        public static void onRightClick(PlayerInteractEvent.RightClickBlock event) {
            Player player = event.getEntity();

            if (player == null)
                return;

            InteractionHand hand = event.getHand();

            if (hand != InteractionHand.MAIN_HAND)
                return;

            Level level = event.getLevel();

            if (level.isClientSide)
                return;

            BlockPos pos = event.getPos();

            boolean isStatueShceme = false;

            for (Data.StatueBuilderData.ShcemeBuilder builder : Data.StatueBuilderData.getShcemeBuilderList())
                if (builder.mainPos().equals(pos) || builder.posList().contains(pos))
                    isStatueShceme = true;

            if (isStatueShceme)
                return;

            BlockState state = event.getLevel().getBlockState(pos);
            ItemStack mainHandStack = player.getMainHandItem();
            ItemStack offHandStack = player.getOffhandItem();

            if (state.is(Blocks.SANDSTONE) && (mainHandStack.is(ItemsRegistry.HAMMER_CARVER.get()) && offHandStack.is(ItemsRegistry.CHISEL_CARVER.get())) || (mainHandStack.is(ItemsRegistry.CHISEL_CARVER.get()) && offHandStack.is(ItemsRegistry.HAMMER_CARVER.get()))) {
                level.destroyBlock(pos, false);
                level.setBlock(pos, BlocksRegistry.STATUE_CUP_UNFINISHED.get().defaultBlockState(), 11);
                DurabilityUtils.hurt(mainHandStack, 5);
                DurabilityUtils.hurt(offHandStack, 5);
            }
        }
    }
}
