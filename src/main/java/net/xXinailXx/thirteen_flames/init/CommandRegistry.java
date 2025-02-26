package net.xXinailXx.thirteen_flames.init;

import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.Registry;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.commands.AbilityArgumentsType;
import net.xXinailXx.thirteen_flames.commands.ThirteenFlamesCommands;

@Mod.EventBusSubscriber
public class CommandRegistry {
    private static final DeferredRegister<ArgumentTypeInfo<?, ?>> COMMAND_ARGUMENTS = DeferredRegister.create(Registry.COMMAND_ARGUMENT_TYPE_REGISTRY, ThirteenFlames.MODID);
    public static final RegistryObject<ArgumentTypeInfo<?, ?>> RELIC_ABILITY = COMMAND_ARGUMENTS.register("relic_ability", () -> ArgumentTypeInfos.registerByClass(AbilityArgumentsType.class, SingletonArgumentInfo.contextFree(AbilityArgumentsType::abilityType)));

    public static void register() {
        COMMAND_ARGUMENTS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        ThirteenFlamesCommands.register(event.getDispatcher());
    }
}
