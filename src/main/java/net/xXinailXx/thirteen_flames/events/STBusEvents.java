package net.xXinailXx.thirteen_flames.events;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.data.IData;
import net.xXinailXx.thirteen_flames.client.gui.GodFaraon.GodFaraonScreenMining;
import net.xXinailXx.thirteen_flames.data.Data;
import net.xXinailXx.thirteen_flames.entity.TestEntity;
import net.xXinailXx.thirteen_flames.init.EntityRegistry;
import net.xXinailXx.thirteen_flames.init.KeyBindingRegistry;

@Mod.EventBusSubscriber
public class STBusEvents {
    @SubscribeEvent
    public static void openFaraonScreen(InputEvent event) {
        if (KeyBindingRegistry.KEY_TEST.isDown() && Minecraft.getInstance().player != null) {
            IData.IGuiLevelingData guiLevelingData = new Data.GuiLevelingData();
            guiLevelingData.setPlayerScreen(true);
            Minecraft.getInstance().setScreen(new GodFaraonScreenMining());
        }
    }

    @Mod.EventBusSubscriber(modid = ThirteenFlames.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ParticleEvent {
        @SubscribeEvent
        public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
            event.put( EntityRegistry.TEST.get(), TestEntity.setAttributes());
        }
    }
}
