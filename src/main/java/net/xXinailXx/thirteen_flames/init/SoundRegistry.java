package net.xXinailXx.thirteen_flames.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.xXinailXx.thirteen_flames.ThirteenFlames;

public class SoundRegistry {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, ThirteenFlames.MODID);

    public static final RegistryObject<SoundEvent> RECORD = SOUNDS.register("record", () -> new SoundEvent(new ResourceLocation(ThirteenFlames.MODID, "record")));
    public static final RegistryObject<SoundEvent> MOON_BOW_SHOT = SOUNDS.register("moon_bow_shot", () -> new SoundEvent(new ResourceLocation(ThirteenFlames.MODID, "moon_bow_shot")));
    public static final RegistryObject<SoundEvent> MOON_BOW_RAIN = SOUNDS.register("moon_bow_rain", () -> new SoundEvent(new ResourceLocation(ThirteenFlames.MODID, "moon_bow_rain")));
    public static final RegistryObject<SoundEvent> MOON_BOW_SPLASH = SOUNDS.register("moon_bow_splash", () -> new SoundEvent(new ResourceLocation(ThirteenFlames.MODID, "moon_bow_splash")));
    public static final RegistryObject<SoundEvent> MOON_BOW_STORM = SOUNDS.register("moon_bow_storm", () -> new SoundEvent(new ResourceLocation(ThirteenFlames.MODID, "moon_bow_storm")));
    public static final RegistryObject<SoundEvent> MOON_BOW_STORM_SHORT = SOUNDS.register("moon_bow_storm_2", () -> new SoundEvent(new ResourceLocation(ThirteenFlames.MODID, "moon_bow_storm_2")));

    public static void register() {
        SOUNDS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
