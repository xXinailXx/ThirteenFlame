package net.xXinailXx.thirteen_flames.init;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.entity.*;

public class EntitiesRegistry {
    private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ThirteenFlames.MODID);

    public static final RegistryObject<EntityType<ShockwaveEntity>> SHOCKWAVE = ENTITIES.register("shockwave", () ->
            EntityType.Builder.<ShockwaveEntity>of(ShockwaveEntity::new, MobCategory.MISC)
                    .sized(1F, 0.1F)
                    .build("shockwave"));
    public static final RegistryObject<EntityType<OreBlockSimulationEntity>> ORE_SIMULATION = ENTITIES.register("ore_simulation", () ->
            EntityType.Builder.<OreBlockSimulationEntity>of(OreBlockSimulationEntity::new, MobCategory.MISC)
                    .sized(1F, 1F)
                    .build("ore_simulation"));
    public static final RegistryObject<EntityType<StatueShcemeEntity>> SHCEME = ENTITIES.register("shceme", () ->
            EntityType.Builder.<StatueShcemeEntity>of(StatueShcemeEntity::new, MobCategory.MISC)
                    .sized(1F, 1F)
                    .build("shceme"));
    public static final RegistryObject<EntityType<PoisonCloundEntity>> POISON_CLOUD = ENTITIES.register("poison_cloud", () ->
            EntityType.Builder.of(PoisonCloundEntity::new, MobCategory.MISC)
                    .sized(1F, 1F)
                    .build("poison_cloud"));
    public static final RegistryObject<EntityType<MoonDischargeEntity>> MOON_DISCHARGE = ENTITIES.register("moon_discharge", () ->
            EntityType.Builder.of(MoonDischargeEntity::new, MobCategory.MISC)
                    .sized(1F, 1F)
                    .build("moon_discharge"));
    public static final RegistryObject<EntityType<MoonCarrierEntity>> MOON_CARRIES = ENTITIES.register("moon_carries", () ->
            EntityType.Builder.of(MoonCarrierEntity::new, MobCategory.MISC)
                    .sized(1F, 1F)
                    .build("moon_carries"));
    public static final RegistryObject<EntityType<MoonProjectileEntity>> MOON_PROJECTILE = ENTITIES.register("moon_projectile", () ->
            EntityType.Builder.of(MoonProjectileEntity::new, MobCategory.MISC)
                    .sized(1F, 1F)
                    .build("moon_projectile"));
    public static final RegistryObject<EntityType<MoonProjectileSpecialEntity>> MOON_PROJECTILE_SPECIAL = ENTITIES.register("moon_projectile_special", () ->
            EntityType.Builder.of(MoonProjectileSpecialEntity::new, MobCategory.MISC)
                    .sized(1F, 1F)
                    .build("moon_projectile_special"));
    public static final RegistryObject<EntityType<MoonRaindropEntity>> MOON_RAINDROP = ENTITIES.register("moon_raindrop", () ->
            EntityType.Builder.of(MoonRaindropEntity::new, MobCategory.MISC)
                    .sized(1F, 1F)
                    .build("moon_raindrop"));
    public static final RegistryObject<EntityType<MoonStormcallerEntity>> MOON_STORMCALLER = ENTITIES.register("moon_stormcaller", () ->
            EntityType.Builder.of(MoonStormcallerEntity::new, MobCategory.MISC)
                    .sized(1F, 1F)
                    .build("moon_stormcaller"));
    public static final RegistryObject<EntityType<MoonStormEntity>> MOON_STORM = ENTITIES.register("moon_storm", () ->
            EntityType.Builder.of(MoonStormEntity::new, MobCategory.MISC)
                    .sized(1F, 1F)
                    .build("moon_storm"));
    public static final RegistryObject<EntityType<LivingFleshEntity>> LIVING_FLESH = ENTITIES.register("living_flesh", () ->
            EntityType.Builder.<LivingFleshEntity>of(LivingFleshEntity::new, MobCategory.MISC)
                    .sized(1F, 1F)
                    .build("living_flesh"));
    public static final RegistryObject<EntityType<SunSeliasetEntity>> SUN_SELIASET = ENTITIES.register("sun_seliaset", () ->
            EntityType.Builder.<SunSeliasetEntity>of(SunSeliasetEntity::new, MobCategory.MISC)
                    .sized(1F, 1F)
                    .build("sun_seliaset"));
    public static final RegistryObject<EntityType<HornSeliasetEntity>> HORN_SELIASET = ENTITIES.register("horn_seliaset", () ->
            EntityType.Builder.<HornSeliasetEntity>of(HornSeliasetEntity::new, MobCategory.MISC)
                    .sized(1F, 1F)
                    .build("horn_seliaset"));
    public static final RegistryObject<EntityType<HornWindSeliasetEntity>> HORN_WIND_SELIASET = ENTITIES.register("horn_wind_seliaset", () ->
            EntityType.Builder.<HornWindSeliasetEntity>of(HornWindSeliasetEntity::new, MobCategory.MISC)
                    .sized(1F, 1F)
                    .build("horn_wind_seliaset"));

    public static void registerEntities() {
        ENTITIES.register( FMLJavaModLoadingContext.get().getModEventBus());
    }
}
