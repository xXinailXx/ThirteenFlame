package net.xXinailXx.thirteen_flames.network.packet;

import it.hurts.sskirillss.relics.items.relics.base.utils.LevelingUtils;
import it.hurts.sskirillss.relics.tiles.ResearchingTableTile;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.xXinailXx.thirteen_flames.item.base.FlameItemSetting;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class AddExpFlamePacket implements IPacket {
    private BlockPos pos;

    public AddExpFlamePacket(BlockPos pos) {
        this.pos = pos;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    public void read(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
    }

    public void serverExecute(PacketContext ctx) {
        ServerPlayer player = ctx.getSender();

        if (player == null)
            return;

        Level level = player.getLevel();

        if (!(level.getChunkAt(pos).getBlockEntities().get(pos) instanceof ResearchingTableTile tile))
            return;

        ItemStack stack = tile.getStack();

        if (!(stack.getItem() instanceof FlameItemSetting))
            return;

        LevelingUtils.addExperience(player, stack, 50);
        level.sendBlockUpdated(pos, level.getBlockState(pos), level.getBlockState(pos), 2);
    }
}
