package net.xXinailXx.thirteen_flames.network.packet;

import daripher.skilltree.capability.skill.IPlayerSkills;
import daripher.skilltree.capability.skill.PlayerSkillsProvider;
import daripher.skilltree.config.Config;
import daripher.skilltree.network.NetworkDispatcher;
import daripher.skilltree.network.message.SyncPlayerSkillsMessage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.List;
import java.util.function.Supplier;

public class SetSkillPointPacket {
    private final int amount;

    public SetSkillPointPacket(int amount) {
        this.amount = amount;
    }

    public SetSkillPointPacket(FriendlyByteBuf buf) {
        this.amount = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.amount);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context)ctx.get()).enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            IPlayerSkills skillsData = PlayerSkillsProvider.get(player);

            skillsData.setSkillPoints(this.amount);
            NetworkDispatcher.network_channel.send(PacketDistributor.PLAYER.with(() -> player), new SyncPlayerSkillsMessage(player));
        });

        return true;
    }
}
