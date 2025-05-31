package net.xXinailXx.thirteen_flames.network.packet;

import daripher.skilltree.capability.skill.IPlayerSkills;
import daripher.skilltree.capability.skill.PlayerSkillsProvider;
import daripher.skilltree.config.Config;
import daripher.skilltree.network.NetworkDispatcher;
import daripher.skilltree.network.message.SyncPlayerSkillsMessage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.PacketContext;

import java.util.List;
import java.util.function.Supplier;

public class AddSkillPointPacket implements IPacket {
    private int amount;

    public AddSkillPointPacket(int amount) {
        this.amount = amount;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.amount);
    }

    public void read(FriendlyByteBuf buf) {
        this.amount = buf.readInt();
    }

    public void serverExecute(PacketContext ctx) {
        ServerPlayer player = ctx.getSender();
        IPlayerSkills skillsData = PlayerSkillsProvider.get(player);

        skillsData.grantSkillPoints(this.amount);
        NetworkDispatcher.network_channel.send(PacketDistributor.PLAYER.with(() -> player), new SyncPlayerSkillsMessage(player));
    }
}
