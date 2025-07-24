package net.xXinailXx.thirteen_flames.network.packet;

import it.hurts.sskirillss.relics.utils.NBTUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.xXinailXx.thirteen_flames.entity.StatueShcemeEntity;
import net.xXinailXx.thirteen_flames.item.MarkupItem;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

import java.util.UUID;

@MainThreaded
public class DiscardShcemeEntityPacket implements IPacket {
    private ItemStack stack;

    public DiscardShcemeEntityPacket(ItemStack stack) {
        this.stack = stack;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeItem(this.stack);
    }

    public void read(FriendlyByteBuf buf) {
        this.stack = buf.readItem();
    }

    public void serverExecute(PacketContext ctx) {
        ServerLevel level = ctx.getSender().getLevel();
        CompoundTag data = this.stack.getTag();

        if (data == null)
            return;

        String uuid = NBTUtils.getString(this.stack, MarkupItem.TAG_SHCEME_UUID, "");

        if (uuid.isEmpty()) {
            this.stack.setTag(null);
            ctx.getSender().setItemSlot(EquipmentSlot.MAINHAND, this.stack);

            return;
        }

        Entity entity = level.getEntity(UUID.fromString(uuid));

        if (!(entity instanceof StatueShcemeEntity shceme)) {
            this.stack.setTag(null);
            ctx.getSender().setItemSlot(EquipmentSlot.MAINHAND, this.stack);

            return;
        } else if (shceme.isBuilded()) {
            this.stack.setTag(null);
            ctx.getSender().setItemSlot(EquipmentSlot.MAINHAND, this.stack);

            return;
        }

        this.stack.setTag(null);
        ctx.getSender().setItemSlot(EquipmentSlot.MAINHAND, this.stack);

        entity.discard();
    }
}
