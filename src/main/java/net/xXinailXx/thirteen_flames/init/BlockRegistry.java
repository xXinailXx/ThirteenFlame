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
import net.xXinailXx.thirteen_flames.item.base.StatueBlockItem;

public class BlockRegistry {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ThirteenFlames.MODID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ThirteenFlames.MODID);

    public static final RegistryObject<Block> AURITEH_BLOCK = BLOCKS.register("auriteh_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(0.5f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> AURITEH_ORE = BLOCKS.register("auriteh_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE).strength(10f).requiresCorrectToolForDrops(), UniformInt.of(3, 7)));
    public static final RegistryObject<Block> LAZOTEP_BLOCK = BLOCKS.register("lazotep_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(0.5f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> LAZOTEP_ORE = BLOCKS.register("lazotep_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE).strength(14f).requiresCorrectToolForDrops(), UniformInt.of(3, 7)));
    public static final RegistryObject<Block> LOOSE_SAND = BLOCKS.register("loose_sands",
            () -> new WebBlock(BlockBehaviour.Properties.of(Material.SAND).noCollission().strength(0.5F)));

    public static final RegistryObject<Block> STATUE_KNEF_STRUCTURE = BLOCKS.register("statue_knef_structure_block", StatueStructureBlock::new);
    public static final RegistryObject<Block> STATUE_SELYA_STRUCTURE = BLOCKS.register("statue_selya_structure_block", StatueStructureBlock::new);
    public static final RegistryObject<Block> STATUE_MONTU_STRUCTURE = BLOCKS.register("statue_montu_structure_block", StatueStructureBlock::new);
    public static final RegistryObject<Block> STATUE_RONOS_STRUCTURE = BLOCKS.register("statue_ronos_structure_block", StatueStructureBlock::new);
    public static final RegistryObject<Block> STATUE_HET_STRUCTURE = BLOCKS.register("statue_het_structure_block", StatueStructureBlock::new);
    public static final RegistryObject<Block> STATUE_GOD_PHARAOH_STRUCTURE = BLOCKS.register("statue_god_pharaoh_structure_block", StatueStructureBlock::new);
    public static final RegistryObject<Block> STATUE_GOD_PHARAOH_UNFINISHED_STRUCTURE = BLOCKS.register("statue_god_pharaoh_unfinished_structure_block", StatueStructureBlock::new);
    public static final RegistryObject<Block> STATUE_KNEF = BLOCKS.register("statue_knef_block", StatueKnef::new);
    public static final RegistryObject<Block> STATUE_SELYA = BLOCKS.register("statue_selya_block", StatueSelya::new);
    public static final RegistryObject<Block> STATUE_MONTU = BLOCKS.register("statue_montu_block", StatueMontu::new);
    public static final RegistryObject<Block> STATUE_RONOS = BLOCKS.register("statue_ronos_block", StatueRonos::new);
    public static final RegistryObject<Block> STATUE_HET = BLOCKS.register("statue_het_block", StatueHet::new);
    public static final RegistryObject<Block> STATUE_GOD_PHARAOH = BLOCKS.register("statue_god_pharaoh_block", StatueGodPharaoh::new);
    public static final RegistryObject<Block> STATUE_KNEF_UNFINISHED = BLOCKS.register("statue_knef_unfinished_block", StatueKnefUnfinished::new);
    public static final RegistryObject<Block> STATUE_SELYA_UNFINISHED = BLOCKS.register("statue_selya_unfinished_block", StatueSelyaUnfinished::new);
    public static final RegistryObject<Block> STATUE_MONTU_UNFINISHED = BLOCKS.register("statue_montu_unfinished_block", StatueMontuUnfinished::new);
    public static final RegistryObject<Block> STATUE_RONOS_UNFINISHED = BLOCKS.register("statue_ronos_unfinished_block", StatueRonosUnfinished::new);
    public static final RegistryObject<Block> STATUE_HET_UNFINISHED = BLOCKS.register("statue_het_unfinished_block", StatueHetUnfinished::new);
    public static final RegistryObject<Block> STATUE_GOD_PHARAOH_UNFINISHED = BLOCKS.register("statue_god_pharaoh_unfinished_block", StatueGodPharaohUnfinished::new);
    public static final RegistryObject<Block> STATUE_CUP = BLOCKS.register("statue_cup", StatueCup::new);
    public static final RegistryObject<Block> STATUE_CUP_UNFINISHED = BLOCKS.register("statue_cup_unfinished", StatueCup::new);

    public static void register() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());

        for (RegistryObject<? extends Block> block : BLOCKS.getEntries())
            ITEMS.register(block.getId().getPath(), () -> new StatueBlockItem(block.get(), new Item.Properties().tab(ThirteenFlames.ITEMS_TAB)));

        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
