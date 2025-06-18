package net.xXinailXx.thirteen_flames.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.PacketContext;

public class ScrollItemUpdatePacket implements IPacket {
    private ItemStack stack;

    public ScrollItemUpdatePacket(ItemStack stack) {
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

        player.setItemSlot(EquipmentSlot.MAINHAND, this.stack);
    }
}
