package net.xXinailXx.thirteen_flames.network.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.xXinailXx.thirteen_flames.block.entity.StatueBE;
import net.xXinailXx.thirteen_flames.data.Data;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.PacketContext;

public class GetStatueBEPacket implements IPacket {
    private BlockPos pos;

    public GetStatueBEPacket(BlockPos pos) {
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

        BlockEntity entity = level.getChunkAt(this.pos).getBlockEntities().get(this.pos);

        if (entity == null)
            return;

        if (!(entity instanceof StatueBE))
            return;

        Data.StatueBuilderData.STATUE_BE = (StatueBE) entity;
    }
}
