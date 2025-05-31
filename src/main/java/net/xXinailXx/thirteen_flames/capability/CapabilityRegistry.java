package net.xXinailXx.thirteen_flames.capability;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.data.Data;
import net.xXinailXx.thirteen_flames.data.IData;
import net.xXinailXx.thirteen_flames.data.StaminaData;
import net.xXinailXx.thirteen_flames.network.packet.capability.*;
import org.zeith.hammerlib.net.Network;

@Mod.EventBusSubscriber
public class CapabilityRegistry {
    public static final Capability<IData> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

    @SubscribeEvent
    public static void register(RegisterCapabilitiesEvent event) {
        event.register(Data.class);
    }

    @SubscribeEvent
    public static void attach(final AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player)
            event.addCapability(new ResourceLocation(ThirteenFlames.MODID, "capability"), new CapabilityProvider());
    }

    @SubscribeEvent
    public static void playerCloned(PlayerEvent.Clone event) {
        IData origial = CapabilityProvider.get(event.getOriginal());
        IData cloneData = CapabilityProvider.get(event.getEntity());

        cloneData.deserializeNBT(origial.serializeNBT());
        StaminaData.Utils.setStaminaData(event.getEntity(), StaminaData.Utils.getStaminaData(event.getOriginal()));

        event.getOriginal().invalidateCaps();
    }

    @SubscribeEvent
    public static void playerLogged(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();

        Network.sendTo(new StaminaSyncPacket(StaminaData.Utils.getStaminaData(player).serializeNBT()), player);
    }

    @SubscribeEvent
    public static void playerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() == null)
            return;

        StaminaData.Utils.setStaminaData(event.getEntity(), StaminaData.Utils.getStaminaData(event.getEntity()));
    }

    @SubscribeEvent
    public static void playerDimensionChange(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() == null)
            return;

        StaminaData.Utils.setStaminaData(event.getEntity(), StaminaData.Utils.getStaminaData(event.getEntity()));
    }
}
