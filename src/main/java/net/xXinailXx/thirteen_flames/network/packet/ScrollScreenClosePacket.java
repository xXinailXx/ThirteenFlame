package net.xXinailXx.thirteen_flames.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.PacketContext;

public class ScrollScreenClosePacket implements IPacket {
    private ItemStack stack;

    public ScrollScreenClosePacket(ItemStack stack) {
        this.stack = stack;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeItem(this.stack);
    }

    public void read(FriendlyByteBuf buf) {
        this.stack = buf.readItem();
    }

    public void serverExecute(PacketContext ctx) {
        ServerPlayer player = ctx.getSender();

        if (player == null)
            return;

        boolean full = true;

        for (ItemStack stack1 : player.getInventory().items) {
            if (stack1.isEmpty()) {
                full = false;
                break;
            }
        }

        if (!full)
            player.addItem(this.stack);
        else
            player.drop(this.stack, false);
    }
}
