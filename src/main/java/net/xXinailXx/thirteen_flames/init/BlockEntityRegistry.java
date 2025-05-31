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

    public static final RegistryObject<BlockEntityType<StatueKnefBE>> STATUE_KNEF = BLOCK_ENTITIES.register("statue_knef_block_entity",
            () -> BlockEntityType.Builder.of(StatueKnefBE::new, BlockRegistry.STATUE_KNEF.get()).build(null));

    public static final RegistryObject<BlockEntityType<StatueSelyaBE>> STATUE_SELYA = BLOCK_ENTITIES.register("statue_selya_block_entity",
            () -> BlockEntityType.Builder.of(StatueSelyaBE::new, BlockRegistry.STATUE_SELYA.get()).build(null));

    public static final RegistryObject<BlockEntityType<StatueMontuBE>> STATUE_MONTU = BLOCK_ENTITIES.register("statue_montu_block_entity",
            () -> BlockEntityType.Builder.of(StatueMontuBE::new, BlockRegistry.STATUE_MONTU.get()).build(null));

    public static final RegistryObject<BlockEntityType<StatueRonosBE>> STATUE_RONOS = BLOCK_ENTITIES.register("statue_ronos_block_entity",
            () -> BlockEntityType.Builder.of(StatueRonosBE::new, BlockRegistry.STATUE_RONOS.get()).build(null));

    public static final RegistryObject<BlockEntityType<StatueHetBE>> STATUE_HET = BLOCK_ENTITIES.register("statue_het_block_entity",
            () -> BlockEntityType.Builder.of(StatueHetBE::new, BlockRegistry.STATUE_HET.get()).build(null));

    public static final RegistryObject<BlockEntityType<StatueGodPharaohBE>> STATUE_GOD_PHARAOH = BLOCK_ENTITIES.register("statue_god_pharaoh_block_entity",
            () -> BlockEntityType.Builder.of(StatueGodPharaohBE::new, BlockRegistry.STATUE_GOD_PHARAOH.get()).build(null));

    public static final RegistryObject<BlockEntityType<StatueKnefUBE>> STATUE_KNEF_UNFINISHED = BLOCK_ENTITIES.register("statue_knef_block_unfinished_entity",
            () -> BlockEntityType.Builder.of(StatueKnefUBE::new, BlockRegistry.STATUE_KNEF_UNFINISHED.get()).build(null));

    public static final RegistryObject<BlockEntityType<StatueSelyaUBE>> STATUE_SELYA_UNFINISHED = BLOCK_ENTITIES.register("statue_selya_block_unfinished_entity",
            () -> BlockEntityType.Builder.of(StatueSelyaUBE::new, BlockRegistry.STATUE_SELYA_UNFINISHED.get()).build(null));

    public static final RegistryObject<BlockEntityType<StatueMontuUBE>> STATUE_MONTU_UNFINISHED = BLOCK_ENTITIES.register("statue_montu_block_unfinished_entity",
            () -> BlockEntityType.Builder.of(StatueMontuUBE::new, BlockRegistry.STATUE_MONTU_UNFINISHED.get()).build(null));

    public static final RegistryObject<BlockEntityType<StatueRonosUbe>> STATUE_RONOS_UNFINISHED = BLOCK_ENTITIES.register("statue_ronos_block_unfinished_entity",
            () -> BlockEntityType.Builder.of(StatueRonosUbe::new, BlockRegistry.STATUE_RONOS_UNFINISHED.get()).build(null));

    public static final RegistryObject<BlockEntityType<StatueHetUBE>> STATUE_HET_UNFINISHED = BLOCK_ENTITIES.register("statue_het_block_unfinished_entity",
            () -> BlockEntityType.Builder.of(StatueHetUBE::new, BlockRegistry.STATUE_HET_UNFINISHED.get()).build(null));

    public static final RegistryObject<BlockEntityType<StatueGodPharaohUBE>> STATUE_GOD_PHARAOH_UNFINISHED = BLOCK_ENTITIES.register("statue_god_pharaoh_block_unfinished_entity",
            () -> BlockEntityType.Builder.of(StatueGodPharaohUBE::new, BlockRegistry.STATUE_GOD_PHARAOH_UNFINISHED.get()).build(null));

    public static void register() {
        BLOCK_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
