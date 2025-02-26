package net.xXinailXx.thirteen_flames.init;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.block.entity.*;

public class BlockEntityRegistry {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ThirteenFlames.MODID);

    public static final RegistryObject<BlockEntityType<StatueKnefBlockEntity>> STATUE_KNEF_BLOCK_ENTITY = BLOCK_ENTITIES.register("statue_knef_block_entity",
            () -> BlockEntityType.Builder.of( StatueKnefBlockEntity::new, BlockRegistry.STATUE_KNEF_BLOCK.get()).build(null));

    public static final RegistryObject<BlockEntityType<StatueSelyaBlockEntity>> STATUE_SELYA_BLOCK_ENTITY = BLOCK_ENTITIES.register("statue_selya_block_entity",
            () -> BlockEntityType.Builder.of( StatueSelyaBlockEntity::new, BlockRegistry.STATUE_SELYA_BLOCK.get()).build(null));

    public static final RegistryObject<BlockEntityType<StatueMontuBlockEntity>> STATUE_MONTU_BLOCK_ENTITY = BLOCK_ENTITIES.register("statue_montu_block_entity",
            () -> BlockEntityType.Builder.of( StatueMontuBlockEntity::new, BlockRegistry.STATUE_MONTU_BLOCK.get()).build(null));

    public static final RegistryObject<BlockEntityType<StatueRonosBlockEntity>> STATUE_RONOS_BLOCK_ENTITY = BLOCK_ENTITIES.register("statue_ronos_block_entity",
            () -> BlockEntityType.Builder.of( StatueRonosBlockEntity::new, BlockRegistry.STATUE_RONOS_BLOCK.get()).build(null));

    public static final RegistryObject<BlockEntityType<StatueHetBlockEntity>> STATUE_HET_BLOCK_ENTITY = BLOCK_ENTITIES.register("statue_het_block_entity",
            () -> BlockEntityType.Builder.of( StatueHetBlockEntity::new, BlockRegistry.STATUE_HET_BLOCK.get()).build(null));

    public static final RegistryObject<BlockEntityType<GodFaraonBlockEntity>> GOD_FARAON_BLOCK_ENTITY = BLOCK_ENTITIES.register("god_faraon_block_entity",
            () -> BlockEntityType.Builder.of( GodFaraonBlockEntity::new, BlockRegistry.GOD_FARAON_BLOCK.get()).build(null));

    public static final RegistryObject<BlockEntityType<SunSeliasetBlockEntity>> SUN_SELIASET_BLOCK_ENTITY = BLOCK_ENTITIES.register("sun_seliaset_block_entity",
            () -> BlockEntityType.Builder.of( SunSeliasetBlockEntity::new, BlockRegistry.SUN_SELIASET_BLOCK.get()).build(null));

    public static void register() {
        BLOCK_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
