package net.xXinailXx.thirteen_flames.init;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.client.gui.scroll.ScrollMenu;

public class MenusRegistry {
    private static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, ThirteenFlames.MODID);

    public static final RegistryObject<MenuType<ScrollMenu>> SCROLL_MENU = MENUS.register("scroll_menu", () -> IForgeMenuType.create(ScrollMenu::new));

    public static void register() {
        MENUS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
