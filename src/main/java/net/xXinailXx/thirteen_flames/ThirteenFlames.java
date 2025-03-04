package net.xXinailXx.thirteen_flames;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.xXinailXx.thirteen_flames.entity.TestEntityRenderer;
import net.xXinailXx.thirteen_flames.init.*;
import net.xXinailXx.thirteen_flames.world.feature.CongiruredFeatures;
import software.bernie.geckolib3.GeckoLib;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;

@Mod(ThirteenFlames.MODID)
public class ThirteenFlames {
    public static final String MODID = "thirteen_flames";
    public static final CreativeModeTab FLAME_TAB = new CreativeModeTab("thirteen_flames_flame_tab") {
        public ItemStack makeIcon() {
            return ItemsRegistry.MOLOT_MONTU.get().getDefaultInstance();
        }
    };
    public static final CreativeModeTab ITEMS_TAB = new CreativeModeTab("thirteen_flames_items_tab") {
        public ItemStack makeIcon() {
            return ItemsRegistry.LAZOTEP_INGOT.get().getDefaultInstance();
        }
    };

    public ThirteenFlames() {
        MinecraftForge.EVENT_BUS.register(this);

        ItemsRegistry.register();
        BlockRegistry.register();
        BlockEntityRegistry.register();
        CommandRegistry.register();
        EffectsRegistry.register();
        SoundsRegistry.register();
        EntityRegistry.registerEntities();
        CongiruredFeatures.register();

        GeckoLib.initialize();
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(EntityRegistry.TEST.get(), TestEntityRenderer::new);
        }
    }
}
