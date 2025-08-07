package net.xXinailXx.thirteen_flames.network.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.xXinailXx.enderdragonlib.utils.statues.data.StatueData;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.PacketContext;

public class DestroyStatuePacket implements IPacket {
    private BlockPos pos;

    public DestroyStatuePacket(BlockPos pos) {
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
        StatueData.StatueBuilder builder = StatueData.getStatue(pos);

        if (builder == null) {
            level.destroyBlock(pos, true);

            return;
        }

        for (BlockPos pos1 : builder.posList())
            level.setBlock(pos1, Blocks.AIR.defaultBlockState(), 11);

        if (!ctx.getSender().isCreative())
            level.destroyBlock(builder.mainPos(), true);
        else
            level.setBlock(builder.mainPos(), Blocks.AIR.defaultBlockState(), 11);

        StatueData.removeStatue(builder.mainPos());
    }
}
