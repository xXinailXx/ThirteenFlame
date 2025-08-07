package net.xXinailXx.thirteen_flames.network.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.xXinailXx.enderdragonlib.utils.statues.CustomStatueUtils;
import net.xXinailXx.enderdragonlib.utils.statues.data.StatueData;
import net.xXinailXx.thirteen_flames.block.StatueHandler;
import net.xXinailXx.thirteen_flames.block.StatueStructureBlock;
import net.xXinailXx.thirteen_flames.block.entity.StatueBE;
import net.xXinailXx.thirteen_flames.init.BlockRegistry;
import net.xXinailXx.thirteen_flames.init.ItemRegistry;
import net.xXinailXx.thirteen_flames.item.BagPaintItem;
import net.xXinailXx.thirteen_flames.utils.Gods;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.PacketContext;

public class UpgradeStatuePacket implements IPacket {
    private BlockPos pos;
    private ItemStack stack;

    public UpgradeStatuePacket(BlockPos pos, ItemStack stack) {
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
        ServerPlayer player = ctx.getSender();
        ServerLevel level = player.getLevel();

        if (level.getBlockState(this.pos).getBlock() instanceof StatueStructureBlock) {
            StatueData.StatueBuilder builder = StatueData.getStatue(this.pos);

            if (builder == null)
                return;

            this.pos = builder.mainPos();
        }

        StatueBE be = (StatueBE) level.getChunkAt(this.pos).getBlockEntity(this.pos);

        if (be == null)
            return;

        Direction direction = level.getBlockState(this.pos).getValue(CustomStatueUtils.FACING);

        if (!be.isFinished()) {
            if (this.stack.getItem() instanceof BagPaintItem item && !item.getGod().equals(be.getGod()))
                return;

            if (be.getGod().equals(Gods.GOD_PHARAOH)) {
                CustomStatueUtils newStatue = (CustomStatueUtils) BlockRegistry.STATUE_GOD_PHARAOH.get();

                player.getLevel().setBlock(this.pos, newStatue.defaultBlockState().setValue(CustomStatueUtils.FACING, direction), 11);
            } else {
                StatueHandler handler = (StatueHandler) player.getLevel().getBlockState(this.pos).getBlock();
                StatueHandler newHadler = null;

                switch (handler.getGod()) {
                    case KNEF -> newHadler = (StatueHandler) BlockRegistry.STATUE_KNEF.get();
                    case SELYA -> newHadler = (StatueHandler) BlockRegistry.STATUE_SELYA.get();
                    case MONTU -> newHadler = (StatueHandler) BlockRegistry.STATUE_MONTU.get();
                    case RONOS -> newHadler = (StatueHandler) BlockRegistry.STATUE_RONOS.get();
                    case HET -> newHadler = (StatueHandler) BlockRegistry.STATUE_HET.get();
                }

                player.getLevel().setBlock(this.pos, newHadler.defaultBlockState().setValue(CustomStatueUtils.FACING, direction), 11);
            }

            if (this.stack.getItem() instanceof BagPaintItem && !player.isCreative())
                player.setItemInHand(InteractionHand.MAIN_HAND, ItemRegistry.BAG_PAINT.get().getDefaultInstance());
        } else if (this.stack.is(ItemRegistry.STATUE_UPGRADER.get())) {
            if (be.getTimeToUpgrade() > 0)
                be.setTimeToUpgrade(0);
            else
                be.resetFlameUpgradeData();
        }
    }
}
