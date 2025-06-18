package net.xXinailXx.thirteen_flames.init;

import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.xXinailXx.thirteen_flames.ThirteenFlames;

import java.util.List;

public class FeaturesRegistry {
    public static final DeferredRegister<ConfiguredFeature<?, ?>> FEATURES = DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, ThirteenFlames.MODID);
    public static final DeferredRegister<PlacedFeature> PLACED_FEATURES = DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, ThirteenFlames.MODID);

    public static final RegistryObject<ConfiguredFeature<?, ?>> AURITEH_ORE;
    public static final RegistryObject<PlacedFeature> AURITEH_ORE_PLACED;
    public static final RegistryObject<ConfiguredFeature<?, ?>> LAZOTEP_ORE;
    public static final RegistryObject<PlacedFeature> LAZOTEP_ORE_PLACED;

    static {
        AURITEH_ORE = FEATURES.register("auriteh_ore",
                () -> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(List.of(
                        OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, BlocksRegistry.AURITEH_ORE.get().defaultBlockState())),6)));
        AURITEH_ORE_PLACED = PLACED_FEATURES.register("auriteh_ore_placed", () -> new PlacedFeature(AURITEH_ORE.getHolder().get(),
                commonOrePlacement(6, HeightRangePlacement.uniform(VerticalAnchor.absolute(40), VerticalAnchor.absolute(100)))));

        LAZOTEP_ORE = FEATURES.register("lazotep_ore",
                () -> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(List.of(
                        OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, BlocksRegistry.LAZOTEP_ORE.get().defaultBlockState())),6)));
        LAZOTEP_ORE_PLACED = PLACED_FEATURES.register("lazotep_ore_placed", () -> new PlacedFeature(LAZOTEP_ORE.getHolder().get(),
                commonOrePlacement(6, HeightRangePlacement.uniform(VerticalAnchor.absolute(40), VerticalAnchor.absolute(100)))));
    }

    public static List<PlacementModifier> orePlacement(PlacementModifier p_195347_, PlacementModifier p_195348_) {
        return List.of(p_195347_, InSquarePlacement.spread(), p_195348_, BiomeFilter.biome());
    }

    public static List<PlacementModifier> commonOrePlacement(int p_195344_, PlacementModifier p_195345_) {
        return orePlacement(CountPlacement.of(p_195344_), p_195345_);
    }

    public static List<PlacementModifier> rareOrePlacement(int p_195350_, PlacementModifier p_195351_) {
        return orePlacement(RarityFilter.onAverageOnceEvery(p_195350_), p_195351_);
    }

    public static void register() {
        FEATURES.register(FMLJavaModLoadingContext.get().getModEventBus());
        PLACED_FEATURES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
