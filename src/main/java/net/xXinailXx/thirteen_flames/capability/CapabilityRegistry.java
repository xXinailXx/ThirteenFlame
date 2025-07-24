package net.xXinailXx.thirteen_flames.capability;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.data.Data;
import net.xXinailXx.thirteen_flames.data.StaminaData;
import net.xXinailXx.thirteen_flames.network.packet.capability.*;
import org.zeith.hammerlib.net.Network;

@Mod.EventBusSubscriber
public class CapabilityRegistry {
    @SubscribeEvent
    public static void playerCloned(PlayerEvent.Clone event) {
        StaminaData.Utils.setStaminaData(event.getEntity(), StaminaData.Utils.getStaminaData(event.getOriginal()));
        Data.AbilitiesData.setAbilitiesData(event.getEntity(), Data.AbilitiesData.getAbilitiesData(event.getOriginal()));
        Data.EffectData.Utils.setEffectData(event.getEntity(), Data.EffectData.Utils.getEffectData(event.getOriginal()));
        Data.GuiLevelingData.Utils.setGuiData(event.getEntity(), Data.GuiLevelingData.Utils.getGuiData(event.getOriginal()));
        Data.XpScarabsData.Utils.setXpScarabsData(event.getEntity(), Data.XpScarabsData.Utils.getXpScarabsData(event.getOriginal()));
        Data.ScarabsData.Utils.setScarabsData(event.getEntity(), Data.ScarabsData.Utils.getScarabsData(event.getOriginal()));

        event.getOriginal().invalidateCaps();
    }

    @SubscribeEvent
    public static void playerLogged(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();

        if (player == null)
            return;

        Network.sendTo(new StaminaSyncPacket(StaminaData.Utils.getStaminaData(player).serializeNBT()), player);
        Network.sendTo(new AbilitiesSyncPacket(Data.AbilitiesData.getAbilitiesData(player)), player);
        Network.sendTo(new EffectSyncPacket(Data.EffectData.Utils.getEffectData(player).serializeNBT()), player);
        Network.sendTo(new GuiSyncPacket(Data.GuiLevelingData.Utils.getGuiData(player).serializeNBT()), player);
        Network.sendTo(new XpScarabsSyncPacket(Data.XpScarabsData.Utils.getXpScarabsData(player).serializeNBT()), player);
        Network.sendTo(new ScarabsSyncPacket(Data.ScarabsData.Utils.getScarabsData(player).serializeNBT()), player);
    }

    @SubscribeEvent
    public static void playerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() == null)
            return;

        StaminaData.Utils.setStaminaData(event.getEntity(), StaminaData.Utils.getStaminaData(event.getEntity()));
        Data.AbilitiesData.setAbilitiesData(event.getEntity(), Data.AbilitiesData.getAbilitiesData(event.getEntity()));
        Data.EffectData.Utils.setEffectData(event.getEntity(), Data.EffectData.Utils.getEffectData(event.getEntity()));
        Data.GuiLevelingData.Utils.setGuiData(event.getEntity(), Data.GuiLevelingData.Utils.getGuiData(event.getEntity()));
        Data.XpScarabsData.Utils.setXpScarabsData(event.getEntity(), Data.XpScarabsData.Utils.getXpScarabsData(event.getEntity()));
        Data.ScarabsData.Utils.setScarabsData(event.getEntity(), Data.ScarabsData.Utils.getScarabsData(event.getEntity()));
    }

    @SubscribeEvent
    public static void playerDimensionChange(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() == null)
            return;

        StaminaData.Utils.setStaminaData(event.getEntity(), StaminaData.Utils.getStaminaData(event.getEntity()));
        Data.AbilitiesData.setAbilitiesData(event.getEntity(), Data.AbilitiesData.getAbilitiesData(event.getEntity()));
        Data.EffectData.Utils.setEffectData(event.getEntity(), Data.EffectData.Utils.getEffectData(event.getEntity()));
        Data.GuiLevelingData.Utils.setGuiData(event.getEntity(), Data.GuiLevelingData.Utils.getGuiData(event.getEntity()));
        Data.XpScarabsData.Utils.setXpScarabsData(event.getEntity(), Data.XpScarabsData.Utils.getXpScarabsData(event.getEntity()));
        Data.ScarabsData.Utils.setScarabsData(event.getEntity(), Data.ScarabsData.Utils.getScarabsData(event.getEntity()));
    }
}
