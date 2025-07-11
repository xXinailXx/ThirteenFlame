package net.xXinailXx.thirteen_flames.network.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.xXinailXx.thirteen_flames.block.*;
import net.xXinailXx.thirteen_flames.block.entity.StatueBE;
import net.xXinailXx.thirteen_flames.data.Data;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.PacketContext;

public class CreativeUpdateStatuePacket implements IPacket {
    private BlockPos pos;

    public CreativeUpdateStatuePacket(BlockPos pos) {
        this.pos = pos;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    public void read(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
    }

    public void serverExecute(PacketContext ctx) {
        ServerLevel level = ctx.getSender().getLevel();
        Data.StatueBuilderData.StatueBuilder builder = Data.StatueBuilderData.getStatue(this.pos);

        if (builder == null)
            return;

        StatueHandler handler = (StatueHandler) level.getBlockState(builder.mainPos()).getBlock();

        if (handler == null || handler.getBE(builder.mainPos()) == null)
            return;

        StatueBE be = handler.getBE(builder.mainPos());

        if (be.getTimeToUpgrade() > 0)
            be.setTimeToUpgrade(0);
        else
            be.resetFlameUpgradeData();
    }
}
