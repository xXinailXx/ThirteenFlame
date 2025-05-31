package net.xXinailXx.thirteen_flames.network.packet.capability;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import net.xXinailXx.thirteen_flames.data.Data;
import net.xXinailXx.thirteen_flames.data.StaminaData;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.PacketContext;

import java.util.function.Supplier;

public class StaminaSyncPacket implements IPacket {
    private CompoundTag nbt;

    public StaminaSyncPacket(CompoundTag nbt) {
        this.nbt = nbt;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeNbt(this.nbt);
    }

    public void read(FriendlyByteBuf buf) {
        this.nbt = buf.readNbt();
    }

    @OnlyIn(Dist.CLIENT)
    public void clientExecute(PacketContext ctx) {
        LocalPlayer player = Minecraft.getInstance().player;

        if (player != null) {
            StaminaData fake = new StaminaData();
            fake.deserializeNBT(this.nbt);
            StaminaData.Utils.setStaminaData(player, fake);
        }
    }
}
