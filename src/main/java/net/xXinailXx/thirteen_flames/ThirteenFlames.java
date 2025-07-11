package net.xXinailXx.thirteen_flames;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.xXinailXx.thirteen_flames.client.gui.scroll.ScrollScreen;
import net.xXinailXx.thirteen_flames.config.ThirteenFlamesConfig;
import net.xXinailXx.thirteen_flames.init.*;
import software.bernie.geckolib3.GeckoLib;

@Mod(ThirteenFlames.MODID)
public class ThirteenFlames {
    public static final String MODID = "thirteen_flames";
    public static final CreativeModeTab FLAME_TAB = new CreativeModeTab("thirteen_flames_flame_tab") {
        public ItemStack makeIcon() {
            return ItemRegistry.HAMMER_MONTU.get().getDefaultInstance();
        }
    };
    public static final CreativeModeTab ITEMS_TAB = new CreativeModeTab("thirteen_flames_items_tab") {
        public ItemStack makeIcon() {
            return ItemRegistry.LAZOTEP_INGOT.get().getDefaultInstance();
        }
    };
    public static final CreativeModeTab STATUES_TAB = new CreativeModeTab("thirteen_flames_statues_tab") {
        public ItemStack makeIcon() {
            return BlockRegistry.STATUE_CUP.get().asItem().getDefaultInstance();
        }
    };

    public ThirteenFlames() {
        MinecraftForge.EVENT_BUS.register(this);

        ItemRegistry.register();
        BlockRegistry.register();
        BlockEntityRegistry.register();
        CommandRegistry.register();
        EffectRegistry.register();
        SoundRegistry.register();
        EntityRegistry.registerEntities();
        FeatureRegistry.register();
        MenuRegistry.register();

        GeckoLib.initialize();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ThirteenFlamesConfig.SPEC, "thirteen_flames-common.toml");
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        MenuScreens.register(MenuRegistry.SCROLL_MENU.get(), ScrollScreen::new);
    }
}
