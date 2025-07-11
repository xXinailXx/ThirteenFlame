package net.xXinailXx.thirteen_flames.network.packet;

import daripher.skilltree.capability.skill.IPlayerSkills;
import daripher.skilltree.capability.skill.PlayerSkillsProvider;
import daripher.skilltree.client.SkillTreeClientData;
import daripher.skilltree.client.screen.SkillTreeScreen;
import daripher.skilltree.network.NetworkDispatcher;
import daripher.skilltree.network.message.SyncPlayerSkillsMessage;
import daripher.skilltree.skill.PassiveSkill;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.PacketDistributor;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.Network;
import org.zeith.hammerlib.net.PacketContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class SetSkillPointPacket implements IPacket {
    private int count;

    public SetSkillPointPacket(int count) {
        this.count = count;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.count);
    }

    public void read(FriendlyByteBuf buf) {
        this.count = buf.readInt();
    }

    public void serverExecute(PacketContext ctx) {
        ServerPlayer player = ctx.getSender();
        IPlayerSkills playerSkills = PlayerSkillsProvider.get(player);

        playerSkills.setSkillPoints(this.count);

        NetworkDispatcher.network_channel.send(PacketDistributor.PLAYER.with(() -> player), new SyncPlayerSkillsMessage(player));
    }
}
