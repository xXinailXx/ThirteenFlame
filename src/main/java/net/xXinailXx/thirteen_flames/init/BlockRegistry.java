package net.xXinailXx.thirteen_flames.init;

import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.WebBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.block.*;

public class BlockRegistry {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ThirteenFlames.MODID);
    private static final DeferredRegister<Block> BLOCKS_NOT_TAB = DeferredRegister.create(ForgeRegistries.BLOCKS, ThirteenFlames.MODID);
    private static final DeferredRegister<Block> SUN_BLOCK_ITEM = DeferredRegister.create(ForgeRegistries.BLOCKS, ThirteenFlames.MODID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ThirteenFlames.MODID);

    public static final RegistryObject<Block> AURITEH_BLOCK = BLOCKS.register("auriteh_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(0.5f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> AURITEH_ORE = BLOCKS.register("auriteh_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE).strength(10f).requiresCorrectToolForDrops()
                    , UniformInt.of(3, 7)));
    public static final RegistryObject<Block> LAZOTEP_BLOCK = BLOCKS.register("lazotep_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(0.5f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> LAZOTEP_ORE = BLOCKS.register("lazotep_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE).strength(14f).requiresCorrectToolForDrops()
                    , UniformInt.of(3, 7)));
    public static final RegistryObject<Block> LOOSE_SAND = BLOCKS.register("loose_sands",
            () -> new WebBlock(BlockBehaviour.Properties.of(Material.SAND).noCollission().strength(0.8F)));

    public static final RegistryObject<StatueKnef> STATUE_KNEF_BLOCK = BLOCKS.register("statue_knef_block", StatueKnef::new);
    public static final RegistryObject<StatueStructureBlock> STATUE_KNEF_STRUCTURE_BLOCK = BLOCKS_NOT_TAB.register("statue_knef_block_structure", StatueStructureBlock::new);

    public static final RegistryObject<StatueSelya> STATUE_SELYA_BLOCK = BLOCKS.register("statue_selya_block", StatueSelya::new);
    public static final RegistryObject<StatueStructureBlock> STATUE_SELYA_STRUCTURE_BLOCK = BLOCKS_NOT_TAB.register("statue_selya_block_structure", StatueStructureBlock::new);

    public static final RegistryObject<StatueMontu> STATUE_MONTU_BLOCK = BLOCKS.register("statue_montu_block", StatueMontu::new);
    public static final RegistryObject<StatueStructureBlock> STATUE_MONTU_STRUCTURE_BLOCK = BLOCKS_NOT_TAB.register("statue_montu_block_structure", StatueStructureBlock::new);

    public static final RegistryObject<StatueRonos> STATUE_RONOS_BLOCK = BLOCKS.register("statue_ronos_block", StatueRonos::new);
    public static final RegistryObject<StatueStructureBlock> STATUE_RONOS_STRUCTURE_BLOCK = BLOCKS_NOT_TAB.register("statue_ronos_block_structure", StatueStructureBlock::new);

    public static final RegistryObject<StatueHet> STATUE_HET_BLOCK = BLOCKS.register("statue_het_block", StatueHet::new);
    public static final RegistryObject<StatueStructureBlock> STATUE_HET_STRUCTURE_BLOCK = BLOCKS_NOT_TAB.register("statue_het_block_structure", StatueStructureBlock::new);

    public static final RegistryObject<GodFaraon> GOD_FARAON_BLOCK = BLOCKS.register("god_faraon_block", GodFaraon::new);
    public static final RegistryObject<GodFaraonStructureBlock> GOD_FARAON_STRUCTURE_BLOCK = BLOCKS_NOT_TAB.register("god_faraon_block_structure", GodFaraonStructureBlock::new);

    public static final RegistryObject<SunSeliasetBlock> SUN_SELIASET_BLOCK = BLOCKS_NOT_TAB.register("sun_seliaset_block", SunSeliasetBlock::new);

    public static void register() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        BLOCKS_NOT_TAB.register(FMLJavaModLoadingContext.get().getModEventBus());

        for (RegistryObject<? extends Block> block : BLOCKS.getEntries())
            ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties().tab(ThirteenFlames.ITEMS_TAB)));

        for (RegistryObject<? extends Block> block : BLOCKS_NOT_TAB.getEntries())
            ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));

        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
