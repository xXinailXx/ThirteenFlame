package net.xXinailXx.thirteen_flames.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.simple.SimpleChannel;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.network.packet.AddSkillPointPacket;
import net.xXinailXx.thirteen_flames.network.packet.SetSkillPointPacket;
import net.xXinailXx.thirteen_flames.network.packet.capability.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = ThirteenFlames.MODID)
public class NetworkRegistry {
    public static SimpleChannel INSTANCE;
    private static int ID = 0;

    private static int nextID() {
        return ID++;
    }

    @SubscribeEvent
    public static void registerNetworkChannel(FMLCommonSetupEvent event) {
        INSTANCE = net.minecraftforge.network.NetworkRegistry.newSimpleChannel(
                new ResourceLocation(ThirteenFlames.MODID, "network"),
                () -> "1.0",
                (s) -> true,
                (s) -> true);
        INSTANCE.messageBuilder(AddSkillPointPacket.class, nextID())
                .encoder(AddSkillPointPacket::toBytes)
                .decoder(AddSkillPointPacket::new)
                .consumer(AddSkillPointPacket::handle)
                .add();
        INSTANCE.messageBuilder(SetSkillPointPacket.class, nextID())
                .encoder(SetSkillPointPacket::toBytes)
                .decoder(SetSkillPointPacket::new)
                .consumer(SetSkillPointPacket::handle)
                .add();
        INSTANCE.messageBuilder(StaminaSyncPacket.class, nextID())
                .encoder(StaminaSyncPacket::toBytes)
                .decoder(StaminaSyncPacket::new)
                .consumer(StaminaSyncPacket::handle)
                .add();
    }

    public static void sendToClient(Object packet, ServerPlayer player) {
        INSTANCE.sendTo(packet, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendToServer(Object packet) {
        INSTANCE.sendToServer(packet);
    }
}
