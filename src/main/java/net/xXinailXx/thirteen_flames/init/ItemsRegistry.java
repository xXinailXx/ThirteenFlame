package net.xXinailXx.thirteen_flames.init;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.xXinailXx.thirteen_flames.item.*;
import net.xXinailXx.thirteen_flames.item.base.ArmorMaterialsTF;
import net.xXinailXx.thirteen_flames.item.flame.*;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.utils.*;

public class ItemsRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ThirteenFlames.MODID);

    public static final RegistryObject<Item> HAMMER_MONTU = ITEMS.register("hammer_montu", HammerMontu::new);
    public static final RegistryObject<Item> GLOVES_MONTU = ITEMS.register("gloves_montu", GlovesMontu::new);
    public static final RegistryObject<Item> SWORD_RONOSA = ITEMS.register("sword_ronosa", SwordRonosa::new);
    public static final RegistryObject<Item> SHIELD_RONOSA = ITEMS.register("shield_ronosa", ShieldRonosa::new);
    public static final RegistryObject<Item> MOON_BOW = ITEMS.register("moon_bow", MoonBow::new);
    public static final RegistryObject<Item> BLACK_ROSE = ITEMS.register("black_rose", BlackRose::new);
    public static final RegistryObject<Item> HORN_SELIASET = ITEMS.register("horn_seliaset", HornSeliaset::new);
    public static final RegistryObject<Item> SUN_SELIASET = ITEMS.register("sun_seliaset", SunSeliaset::new);
    public static final RegistryObject<Item> FLIGHT_HET = ITEMS.register("flight_het", FlightHet::new);
    public static final RegistryObject<Item> SCROLL_HET = ITEMS.register("scroll_het", ScrollHet::new);
    public static final RegistryObject<Item> TRAVELERS_SWORD = ITEMS.register("travelers_sword", TravelersSword::new);
    public static final RegistryObject<Item> STAFF_JODAH = ITEMS.register("staff_jodah", StaffJodah::new);
    public static final RegistryObject<Item> MASK_DEMIURG = ITEMS.register("mask_demiurg", MaskDemiurg::new);

    public static final RegistryObject<Item> RAW_AURITEH = ITEMS.register("raw_auriteh", ItemSetting::new);
    public static final RegistryObject<Item> AURITEH_INGOT = ITEMS.register("auriteh_ingot", ItemSetting::new);
    public static final RegistryObject<Item> AURITEH_NUGGET = ITEMS.register("auriteh_nugget", ItemSetting::new);
    public static final RegistryObject<Item> RAW_LAZOTEP = ITEMS.register("raw_lazotep", ItemSetting::new);
    public static final RegistryObject<Item> LAZOTEP_INGOT = ITEMS.register("lazotep_ingot", ItemSetting::new);
    public static final RegistryObject<Item> LAZOTEP_NUGGET = ITEMS.register("lazotep_nugget", ItemSetting::new);
    public static final RegistryObject<Item> UTEN = ITEMS.register("uten", ItemSetting::new);
    public static final RegistryObject<Item> BAG_PAINT = ITEMS.register("bag_paint", () -> new BagPaintUtils(null));
    public static final RegistryObject<Item> BAG_PAINT_KNEF = ITEMS.register("bag_paint_knef", () -> new BagPaintUtils(Gods.KNEF));
    public static final RegistryObject<Item> BAG_PAINT_SELYA = ITEMS.register("bag_paint_selya", () -> new BagPaintUtils(Gods.SELYA));
    public static final RegistryObject<Item> BAG_PAINT_MONTU = ITEMS.register("bag_paint_montu", () -> new BagPaintUtils(Gods.MONTU));
    public static final RegistryObject<Item> BAG_PAINT_RONOS = ITEMS.register("bag_paint_ronos", () -> new BagPaintUtils(Gods.RONOS));
    public static final RegistryObject<Item> BAG_PAINT_HET = ITEMS.register("bag_paint_het", () -> new BagPaintUtils(Gods.HET));
    public static final RegistryObject<Item> BAG_PAINT_GOD_PHARAOH = ITEMS.register("bag_paint_god_pharaoh", () -> new BagPaintUtils(Gods.GOD_PHARAOH));
    public static final RegistryObject<Item> BAG_PAINT_CUP = ITEMS.register("bag_paint_cup", () -> new BagPaintUtils(null));
    public static final RegistryObject<Item> HAMMER_CARVER = ITEMS.register("hammer_carver", () -> new ItemSetting(new Item.Properties().tab(ThirteenFlames.ITEMS_TAB).stacksTo(1).durability(100).craftRemainder(Items.OAK_LOG)));
    public static final RegistryObject<Item> CHISEL_CARVER = ITEMS.register("chisel_carver", () -> new ItemSetting(new Item.Properties().tab(ThirteenFlames.ITEMS_TAB).stacksTo(1).durability(100).craftRemainder(Items.IRON_INGOT)));
    public static final RegistryObject<Item> NARSAFAR = ITEMS.register("narsafar", Narsafar::new);
    public static final RegistryObject<Item> SCARAB_SILVER = ITEMS.register("scarab_silver", () -> new ScarabItemUtils(ScarabsType.SILVER));
    public static final RegistryObject<Item> SCARAB_GOLD = ITEMS.register("scarab_gold", () -> new ScarabItemUtils(ScarabsType.GOLD));
    public static final RegistryObject<Item> SCARAB_AURITEH = ITEMS.register("scarab_auriteh", () -> new ScarabItemUtils(ScarabsType.AURITEH));
    public static final RegistryObject<Item> SCARAB_LAZATEP = ITEMS.register("scarab_lazotep", () -> new ScarabItemUtils(ScarabsType.LAZOTEP));
    public static final RegistryObject<Item> AROMATIC_OIL_JUNIPER_TREE = ITEMS.register("aromatic_oil_juniper_tree",
            () -> new ItemSetting(Component.translatable("item." + ThirteenFlames.MODID + ".aromatic_oil_juniper_tree.tooltip")));
    public static final RegistryObject<Item> AROMATIC_OIL_ROSE = ITEMS.register("aromatic_oil_rose",
            () -> new ItemSetting(Component.translatable("item." + ThirteenFlames.MODID + ".aromatic_oil_rose.tooltip")));
    public static final RegistryObject<Item> AROMATIC_OIL_ROSEWOOD_MINT = ITEMS.register("aromatic_oil_rosewood_mint",
            () -> new ItemSetting(Component.translatable("item." + ThirteenFlames.MODID + ".aromatic_oil_rosewood_mint.tooltip")));
    public static final RegistryObject<Item> AROMATIC_OIL_LILA_GOOSEBERRY = ITEMS.register("aromatic_oil_lila_gooseberry",
            () -> new ItemSetting(Component.translatable("item." + ThirteenFlames.MODID + ".aromatic_oil_lila_gooseberry.tooltip")));
    public static final RegistryObject<Item> MARKUP_KNEF = ITEMS.register("markup_knef", () -> new MarkupItemUtils(Gods.KNEF));
    public static final RegistryObject<Item> MARKUP_SELYA = ITEMS.register("markup_selya", () -> new MarkupItemUtils(Gods.SELYA));
    public static final RegistryObject<Item> MARKUP_MONTU = ITEMS.register("markup_montu", () -> new MarkupItemUtils(Gods.MONTU));
    public static final RegistryObject<Item> MARKUP_RONOS = ITEMS.register("markup_ronos", () -> new MarkupItemUtils(Gods.RONOS));
    public static final RegistryObject<Item> MARKUP_HET = ITEMS.register("markup_het", () -> new MarkupItemUtils(Gods.HET));
    public static final RegistryObject<Item> MARKUP_GOD_PHARAOH = ITEMS.register("markup_god_pharaoh", () -> new MarkupItemUtils(Gods.GOD_PHARAOH));

    public static final RegistryObject<Item> MASK_SALMANA = ITEMS.register("mask_salmana",
            () -> new MaskSalmana(ArmorMaterialsTF.SALMAN, EquipmentSlot.HEAD,
                    new Item.Properties().tab(ThirteenFlames.ITEMS_TAB).defaultDurability(-1).durability(-1)));
    public static final RegistryObject<Item> MASK_TOIFETUN_SILVER = ITEMS.register("toifetun_silver",
            () -> new ArmorItem(ArmorMaterialsTF.TOIFETUN_SILVER, EquipmentSlot.HEAD,
                    new Item.Properties().tab(ThirteenFlames.ITEMS_TAB).durability(-1).defaultDurability(-1)));
    public static final RegistryObject<Item> MASK_TOIFETUN_GOLD = ITEMS.register("toifetun_gold",
            () -> new ArmorItem(ArmorMaterialsTF.TOIFETUN_GOLD, EquipmentSlot.HEAD,
                    new Item.Properties().tab(ThirteenFlames.ITEMS_TAB).durability(-1).defaultDurability(-1)));
    public static final RegistryObject<Item> MASK_TOIFETUN_WHITE = ITEMS.register("toifetun_white",
            () -> new ArmorItem(ArmorMaterialsTF.TOIFETUN_WHITE, EquipmentSlot.HEAD,
                    new Item.Properties().tab(ThirteenFlames.ITEMS_TAB).durability(-1).defaultDurability(-1)));

    public static void register() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
