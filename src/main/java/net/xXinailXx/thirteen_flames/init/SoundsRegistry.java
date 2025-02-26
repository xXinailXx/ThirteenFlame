package net.xXinailXx.thirteen_flames.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.xXinailXx.thirteen_flames.ThirteenFlames;

public class SoundsRegistry {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, ThirteenFlames.MODID);
    public static final RegistryObject<SoundEvent> RECORD = SOUNDS.register("record", () -> {
        return new SoundEvent(new ResourceLocation(ThirteenFlames.MODID, "record"));
    });

    public static void register() {
        SOUNDS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
