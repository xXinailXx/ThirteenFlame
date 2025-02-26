package net.xXinailXx.thirteen_flames.world.feature;

import com.google.common.base.Suppliers;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.init.BlockRegistry;

import java.util.List;
import java.util.function.Supplier;

public class CongiruredFeatures {
    public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURE =
            DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, ThirteenFlames.MODID);

    public static final Supplier<List<OreConfiguration.TargetBlockState>> OVERWORLD_ZIRCON_ORES = Suppliers.memoize(() -> List.of(
            OreConfiguration.target(OreFeatures.NATURAL_STONE, BlockRegistry.AURITEH_ORE.get().defaultBlockState()),
            OreConfiguration.target(OreFeatures.NATURAL_STONE, BlockRegistry.LAZOTEP_ORE.get().defaultBlockState())));

    public static final RegistryObject<ConfiguredFeature<?, ?>> THERTIN_LIGHT_ORE = CONFIGURED_FEATURE.register("thertin_light_ore",
            () -> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(OVERWORLD_ZIRCON_ORES.get(),7)));

    public static void register() {
        CONFIGURED_FEATURE.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
