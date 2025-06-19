package net.xXinailXx.thirteen_flames.utils;

import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.block.StatueHandler;
import net.xXinailXx.thirteen_flames.block.StatueStructureBlock;
import net.xXinailXx.thirteen_flames.init.BlockRegistry;
import net.xXinailXx.thirteen_flames.init.ItemRegistry;
import net.xXinailXx.thirteen_flames.network.packet.UpdateStatueBlockPacket;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.net.Network;

@Getter
public class BagPaintUtils extends ItemSetting {
    private final Gods god;

    public BagPaintUtils(@Nullable Gods god) {
        super(new Properties().tab(ThirteenFlames.ITEMS_TAB).stacksTo(1));
        this.god = god;
    }

    public InteractionResult useOn(UseOnContext use) {
        ItemStack stack = use.getItemInHand();

        if (stack.is(ItemRegistry.BAG_PAINT.get()))
            return InteractionResult.SUCCESS;

        Level level = use.getLevel();

        if (level == null)
            return InteractionResult.FAIL;

        BlockPos pos = use.getClickedPos();
        Block block = level.getBlockState(pos).getBlock();

        if (isFinished(block, pos))
            return InteractionResult.FAIL;

        Network.sendToServer(new UpdateStatueBlockPacket(pos, stack));

        return InteractionResult.SUCCESS;
    }

    private boolean isFinished(Block block, BlockPos pos) {
        if (block instanceof StatueHandler handler)
            return handler.getBE(pos).isFinished();
        else if (block instanceof StatueStructureBlock structureBlock)
            return structureBlock.getMainBlockBE(pos).isFinished();
        else if (block.defaultBlockState().is(BlockRegistry.STATUE_CUP_UNFINISHED.get()))
            return false;
        else
            return false;
    }
}
