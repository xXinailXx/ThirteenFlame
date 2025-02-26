package net.xXinailXx.thirteen_flames.init;

import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = ThirteenFlames.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class KeyBindingRegistry {
    private static final String CATEGORY = Component.translatable("key.category.st_thirteen_lights").getString();
    public static final KeyMapping KEY_TEST = new KeyMapping("key.st_thirteen_lights.open_player_screen", GLFW.GLFW_KEY_P, CATEGORY);

    @SubscribeEvent
    public static void onKeybindingRegistry(RegisterKeyMappingsEvent event) {
        event.register(KEY_TEST);
    }
}
