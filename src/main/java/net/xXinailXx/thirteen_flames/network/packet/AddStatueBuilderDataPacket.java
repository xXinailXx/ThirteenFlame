package net.xXinailXx.thirteen_flames.network.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.xXinailXx.thirteen_flames.block.entity.StatueBE;
import net.xXinailXx.thirteen_flames.data.Data;
import net.xXinailXx.thirteen_flames.utils.Gods;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.PacketContext;

import java.util.ArrayList;
import java.util.List;

public class AddStatueBuilderDataPacket implements IPacket {
    private BlockPos mainPos;
    private Gods god;

    public AddStatueBuilderDataPacket(BlockPos mainPos, Gods god) {
        this.mainPos = mainPos;
        this.god = god;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.mainPos);
        buf.writeEnum(this.god);
    }

    public void read(FriendlyByteBuf buf) {
        this.mainPos = buf.readBlockPos();
        this.god = buf.readEnum(Gods.class);
    }

    public void serverExecute(PacketContext ctx) {
        ServerLevel level = ctx.getSender().getLevel();

        BlockEntity entity = level.getChunkAt(this.mainPos).getBlockEntities().get(this.mainPos);

        if (entity == null)
            return;

        if (!(entity instanceof StatueBE<?>))
            return;

        Data.StatueBuilderData.addStatue(new Data.StatueBuilderData.StatueBuilder(getBlockPoses(), this.mainPos), (StatueBE) entity);
    }

    public List<BlockPos> getBlockPoses() {
        Iterable<BlockPos> iterable = null;

        if (!this.god.equals(Gods.GOD_PHARAOH))
            iterable = BlockPos.betweenClosed(this.mainPos.offset(-1, 0, -1), this.mainPos.offset(1, 4, 1));
        else
            iterable = BlockPos.betweenClosed(this.mainPos.offset(-2, 0, -2), this.mainPos.offset(2, 6, 2));

        List<BlockPos> posList = new ArrayList<>();

        for (BlockPos blockPos : iterable) {
            if (blockPos.equals(this.mainPos))
                continue;

            posList.add(new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ()));
        }

        return posList;
    }
}
