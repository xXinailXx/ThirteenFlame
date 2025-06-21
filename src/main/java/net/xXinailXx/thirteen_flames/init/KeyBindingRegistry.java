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
    private static final String CATEGORY = Component.translatable("key.category.thirteen_flames").getString();
    public static final KeyMapping OPEN_GUI = new KeyMapping("key.thirteen_flames.open_player_screen", GLFW.GLFW_KEY_P, CATEGORY);
    public static final KeyMapping USE_MASK = new KeyMapping("key.thirteen_flames.use_mask_demiurg", GLFW.GLFW_KEY_Y, CATEGORY);

    @SubscribeEvent
    public static void onKeybindingRegistry(RegisterKeyMappingsEvent event) {
        event.register(OPEN_GUI);
        event.register(USE_MASK);
    }
}
