package net.xXinailXx.thirteen_flames.network.packet.capability;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.xXinailXx.thirteen_flames.data.StaminaData;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.PacketContext;

public class StaminaSyncPacket implements IPacket {
    private CompoundTag tag;

    public StaminaSyncPacket(CompoundTag nbt) {
        this.tag = nbt;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeNbt(this.tag);
    }

    public void read(FriendlyByteBuf buf) {
        this.tag = buf.readNbt();
    }

    @OnlyIn(Dist.CLIENT)
    public void clientExecute(PacketContext ctx) {
        LocalPlayer player = Minecraft.getInstance().player;

        if (player != null) {
            StaminaData fake = new StaminaData();

            fake.deserializeNBT(this.tag);
            StaminaData.Utils.setStaminaData(player, fake);
        }
    }
}
