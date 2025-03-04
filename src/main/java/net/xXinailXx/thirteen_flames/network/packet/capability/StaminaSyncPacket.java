package net.xXinailXx.thirteen_flames.network.packet.capability;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.xXinailXx.thirteen_flames.data.Data;
import net.xXinailXx.thirteen_flames.data.StaminaData;

import java.util.function.Supplier;

public class StaminaSyncPacket {
    private final CompoundTag nbt;

    public StaminaSyncPacket(FriendlyByteBuf buf) {
        this.nbt = buf.readNbt();
    }

    public StaminaSyncPacket(StaminaData data) {
        this.nbt = data.serializeNBT();
    }

    public StaminaSyncPacket(CompoundTag nbt) {
        this.nbt = nbt;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeNbt(this.nbt);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context)ctx.get()).enqueueWork(() -> {
            LocalPlayer player = Minecraft.getInstance().player;

            if (player != null) {
                StaminaData fake = new StaminaData();
                fake.deserializeNBT(this.nbt);
                StaminaData.Utils.setStaminaData(player, fake);
            }
        });

        return true;
    }
}
