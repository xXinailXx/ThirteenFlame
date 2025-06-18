package net.xXinailXx.thirteen_flames.network.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.xXinailXx.enderdragonlib.utils.statues.CustomStatueUtils;
import net.xXinailXx.thirteen_flames.block.StatueHandler;
import net.xXinailXx.thirteen_flames.block.StatueStructureBlock;
import net.xXinailXx.thirteen_flames.init.BlocksRegistry;
import net.xXinailXx.thirteen_flames.init.ItemsRegistry;
import net.xXinailXx.thirteen_flames.utils.BagPaintUtils;
import net.xXinailXx.thirteen_flames.utils.Gods;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.PacketContext;

public class UpdateStatueBlockPacket implements IPacket {
    private BlockPos pos;
    private ItemStack stack;

    public UpdateStatueBlockPacket(BlockPos pos, ItemStack stack) {
        this.pos = pos;
        this.stack = stack;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.pos);
        buf.writeItem(this.stack);
    }

    public void read(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
        this.stack = buf.readItem();
    }

    public void serverExecute(PacketContext ctx) {
        if (!(this.stack.getItem() instanceof BagPaintUtils utils))
            return;

        ServerPlayer player = ctx.getSender();
        ServerLevel level = ctx.getSender().getLevel();

        Block block = level.getBlockState(this.pos).getBlock();

        if (utils.getGod() == null && this.stack.is(ItemsRegistry.BAG_PAINT_CUP.get())) {
            if (block.defaultBlockState().is(BlocksRegistry.STATUE_CUP_UNFINISHED.get())) {
                level.setBlock(this.pos, BlocksRegistry.STATUE_CUP.get().defaultBlockState(), 11);

                player.setItemInHand(InteractionHand.MAIN_HAND, ItemsRegistry.BAG_PAINT.get().getDefaultInstance());
            } else {
                return;
            }
        }

        if (block instanceof StatueHandler handler) {
            if (!utils.getGod().equals(handler.getGod()))
                return;

            if (utils.getGod().equals(Gods.GOD_PHARAOH)) {
                CustomStatueUtils newStatue = (CustomStatueUtils) BlocksRegistry.STATUE_GOD_PHARAOH.get();
                Direction direction = level.getBlockState(this.pos).getValue(CustomStatueUtils.FACING);

                level.setBlock(this.pos, newStatue.defaultBlockState().setValue(CustomStatueUtils.FACING, direction), 11);
            } else {
                StatueHandler statueHandler = (StatueHandler) level.getBlockState(this.pos).getBlock();
                StatueHandler newHadler = null;

                switch (statueHandler.getGod()) {
                    case KNEF -> newHadler = (StatueHandler) BlocksRegistry.STATUE_KNEF.get();
                    case SELYA -> newHadler = (StatueHandler) BlocksRegistry.STATUE_SELYA.get();
                    case MONTU -> newHadler = (StatueHandler) BlocksRegistry.STATUE_MONTU.get();
                    case RONOS -> newHadler = (StatueHandler) BlocksRegistry.STATUE_RONOS.get();
                    case HET -> newHadler = (StatueHandler) BlocksRegistry.STATUE_HET.get();
                }

                Direction direction = level.getBlockState(this.pos).getValue(CustomStatueUtils.FACING);
                level.setBlock(this.pos, newHadler.defaultBlockState().setValue(CustomStatueUtils.FACING, direction), 11);
            }
            player.setItemInHand(InteractionHand.MAIN_HAND, ItemsRegistry.BAG_PAINT.get().getDefaultInstance());
        } else if (block instanceof StatueStructureBlock structure) {
            if (!utils.getGod().equals(structure.getMainBlockBE(this.pos).getGod()))
                return;

            if (utils.getGod().equals(Gods.GOD_PHARAOH)) {
                CustomStatueUtils newStatue = (CustomStatueUtils) BlocksRegistry.STATUE_GOD_PHARAOH.get();
                Direction direction = level.getBlockState(structure.getBuilder(this.pos).mainPos()).getValue(CustomStatueUtils.FACING);

                level.setBlock(structure.getBuilder(this.pos).mainPos(), newStatue.defaultBlockState().setValue(CustomStatueUtils.FACING, direction), 11);
            } else {
                StatueHandler handler = (StatueHandler) level.getBlockState(structure.getBuilder(this.pos).mainPos()).getBlock();
                StatueHandler newHadler = null;

                switch (handler.getGod()) {
                    case KNEF -> newHadler = (StatueHandler) BlocksRegistry.STATUE_KNEF.get();
                    case SELYA -> newHadler = (StatueHandler) BlocksRegistry.STATUE_SELYA.get();
                    case MONTU -> newHadler = (StatueHandler) BlocksRegistry.STATUE_MONTU.get();
                    case RONOS -> newHadler = (StatueHandler) BlocksRegistry.STATUE_RONOS.get();
                    case HET -> newHadler = (StatueHandler) BlocksRegistry.STATUE_HET.get();
                }

                Direction direction = level.getBlockState(structure.getBuilder(this.pos).mainPos()).getValue(CustomStatueUtils.FACING);
                level.setBlock(structure.getBuilder(this.pos).mainPos(), newHadler.defaultBlockState().setValue(CustomStatueUtils.FACING, direction), 11);
            }

            player.setItemInHand(InteractionHand.MAIN_HAND, ItemsRegistry.BAG_PAINT.get().getDefaultInstance());
        }
    }
}
