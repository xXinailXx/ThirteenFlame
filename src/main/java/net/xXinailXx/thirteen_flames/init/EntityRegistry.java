package net.xXinailXx.thirteen_flames.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.entity.OreBlockSimulationEntity;
import net.xXinailXx.thirteen_flames.entity.ShockwaveEntity;
import net.xXinailXx.thirteen_flames.entity.TestEntity;

public class EntityRegistry {
    private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ThirteenFlames.MODID);
    public static final RegistryObject<EntityType<ShockwaveEntity>> SHOCKWAVE = ENTITIES.register("shockwave", () ->
            EntityType.Builder.<ShockwaveEntity>of(ShockwaveEntity::new, MobCategory.MISC)
                    .sized(1F, 0.1F)
                    .build("shockwave")
    );
    public static final RegistryObject<EntityType<OreBlockSimulationEntity>> ORE_SIMULATION = ENTITIES.register("ore_simulation", () ->
            EntityType.Builder.<OreBlockSimulationEntity>of(OreBlockSimulationEntity::new, MobCategory.MISC)
                    .sized(1.0F, 1.0F)
                    .build("ore_simulation"));
    public static final RegistryObject<EntityType<TestEntity>> TEST = ENTITIES.register("test", () ->
            EntityType.Builder.of(TestEntity::new, MobCategory.MISC)
                    .sized(1.0F, 1.0F)
                    .build("test"));

    public static void registerEntities() {
        ENTITIES.register( FMLJavaModLoadingContext.get().getModEventBus());
    }
}
