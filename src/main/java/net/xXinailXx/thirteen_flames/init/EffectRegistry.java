package net.xXinailXx.thirteen_flames.init;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.effect.*;
import net.xXinailXx.thirteen_flames.effect.Anemia;

import java.awt.*;

public class EffectRegistry {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, ThirteenFlames.MODID);

    public static final RegistryObject<MobEffect> ANEMIA = EFFECTS.register("anemia", () -> new Anemia(MobEffectCategory.HARMFUL, new Color(93, 0, 0).getRGB()));
    public static final RegistryObject<MobEffect> BLESSING_MONTU = EFFECTS.register("blessing_montu", () -> new BlessingMontu(new Color(4, 196, 10).getRGB()));
    public static final RegistryObject<MobEffect> BLESSING_RONOSA = EFFECTS.register("blessing_ronosa", () -> new BlessingRonosa(new Color(209, 13, 13).getRGB()));
    public static final RegistryObject<MobEffect> BLESSING_KNEF = EFFECTS.register("blessing_knef", () -> new BlessingKnef(new Color(44, 0, 138).getRGB()));
    public static final RegistryObject<MobEffect> BLESSING_SELIASET = EFFECTS.register("blessing_seliaset", () -> new BlessingSeliaset(new Color(255, 136, 0).getRGB()));
    public static final RegistryObject<MobEffect> BLESSING_HET = EFFECTS.register("blessing_het", () -> new BlessingHet(new Color(242, 234, 0).getRGB()));
    public static final RegistryObject<MobEffect> KISS_KNEF = EFFECTS.register("kiss_knef", () -> new KissKnef(MobEffectCategory.HARMFUL, new Color(44, 0, 138).getRGB()));
    public static final RegistryObject<MobEffect> POISON = EFFECTS.register("poison", () -> new Poison(MobEffectCategory.HARMFUL, new Color(0, 93, 2).getRGB()));

    public static void register() {
        EFFECTS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
