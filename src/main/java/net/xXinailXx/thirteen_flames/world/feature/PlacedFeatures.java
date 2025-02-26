package net.xXinailXx.thirteen_flames.world.feature;

import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.xXinailXx.thirteen_flames.ThirteenFlames;

import java.util.List;

public class PlacedFeatures {
    public static final DeferredRegister<PlacedFeature> PLACED_FEATURES =
            DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, ThirteenFlames.MODID);


    public static final RegistryObject<PlacedFeature> THERTIN_LIGHT_ORE_PLACED = PLACED_FEATURES.register("thertin_light_ore_placed",
            () -> new PlacedFeature(CongiruredFeatures.THERTIN_LIGHT_ORE.getHolder().get(),
                    commonOrePlacement(7, // VeinsPerChunk
                            HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80)))));

    public static List<PlacementModifier> orePlacement(PlacementModifier modifier, PlacementModifier modifier1) {
        return List.of(modifier, InSquarePlacement.spread(), modifier1, BiomeFilter.biome());
    }

    public static List<PlacementModifier> commonOrePlacement(int i, PlacementModifier modifier) {
        return orePlacement( CountPlacement.of(i), modifier);
    }

    public static List<PlacementModifier> rareOrePlacement(int i, PlacementModifier modifier) {
        return orePlacement( RarityFilter.onAverageOnceEvery(i), modifier);
    }

    public static void register() {
        PLACED_FEATURES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
