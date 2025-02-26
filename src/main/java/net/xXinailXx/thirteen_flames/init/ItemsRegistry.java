package net.xXinailXx.thirteen_flames.init;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.RecordItem;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.xXinailXx.thirteen_flames.item.*;
import net.xXinailXx.thirteen_flames.item.base.ArmorItemTF;
import net.xXinailXx.thirteen_flames.item.base.tools.ToolTierTF;
import net.xXinailXx.thirteen_flames.item.lights.*;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.utils.ItemSetting;

public class ItemsRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ThirteenFlames.MODID);

    public static final RegistryObject<Item> MOLOT_MONTU = ITEMS.register("molot_montu",
            () -> new MolotMontu(ToolTierTF.THIRTEEN_FLAMES, 3, -2.4f, new Item.Properties()));
    public static final RegistryObject<Item> GLOVES_MONTU = ITEMS.register("gloves_montu",
            () -> new GlovesMontu(new Item.Properties()));
    public static final RegistryObject<Item> KLIK_RONOSA = ITEMS.register("klik_ronosa",
            () -> new KlikRonosa(ToolTierTF.THIRTEEN_FLAMES, 3, -2.4f, new Item.Properties()));
    public static final RegistryObject<Item> SHIELD_RONOSA = ITEMS.register("shield_ronosa",
            () -> new ShieldRonosa(new Item.Properties()));
    public static final RegistryObject<Item> BLACK_ROSE = ITEMS.register("black_rose",
            () -> new BlackRose(new Item.Properties()));
    public static final RegistryObject<Item> MOONBOW = ITEMS.register("moon_bow",
            () -> new MoonBow(new Item.Properties()));
    public static final RegistryObject<Item> GORN_SELEASET = ITEMS.register("gorn_seleaset",
            () -> new GornSeleaset(new Item.Properties()));
    public static final RegistryObject<Item> SUN_SELEASET = ITEMS.register("sun_seliaset",
            () -> new SunSeleaset(new Item.Properties()));
    public static final RegistryObject<Item> FLIGHT_HET = ITEMS.register("flight_het",
            () -> new FlightHet(new Item.Properties()));
    public static final RegistryObject<Item> SCROLL_HET = ITEMS.register("scroll_het",
            () -> new ScrollHet(new Item.Properties()));
    public static final RegistryObject<Item> TRAVELERS_SWORD = ITEMS.register("travelers_sword",
            () -> new TravelersSword(ToolTierTF.THIRTEEN_FLAMES, 3, -2.4f, new Item.Properties()));
    public static final RegistryObject<Item> STAFF_JODAH = ITEMS.register("staff_jodah",
            () -> new StaffJodah(new Item.Properties()));
    public static final RegistryObject<Item> MASK_DEMIURG = ITEMS.register("mask_demiurg",
            () -> new ArmorItemTF(ArmorMaterials.NETHERITE, EquipmentSlot.HEAD, new Item.Properties()));

    public static final RegistryObject<Item> RAW_AURITEH = ITEMS.register("raw_auriteh", () -> new ItemSetting(new Item.Properties()));
    public static final RegistryObject<Item> AURITEH_INGOT = ITEMS.register("auriteh_ingot", () -> new ItemSetting(new Item.Properties()));
    public static final RegistryObject<Item> AURITEH_NUGGET = ITEMS.register("auriteh_nugget", () -> new ItemSetting(new Item.Properties()));
    public static final RegistryObject<Item> RAW_LAZOTEP = ITEMS.register("raw_lazotep", () -> new ItemSetting(new Item.Properties()));
    public static final RegistryObject<Item> LAZOTEP_INGOT = ITEMS.register("lazotep_ingot", () -> new ItemSetting(new Item.Properties()));
    public static final RegistryObject<Item> LAZOTEP_NUGGET = ITEMS.register("lazotep_nugget", () -> new ItemSetting(new Item.Properties()));
    public static final RegistryObject<Item> UTEN = ITEMS.register("uten", () -> new ItemSetting(new Item.Properties()));
    public static final RegistryObject<Item> NARSAFAR = ITEMS.register("narsafar", () -> new Narsafar(new Item.Properties()));
    public static final RegistryObject<Item> SCARAB_SILVER = ITEMS.register("scarab_silver", () -> new ItemSetting(new Item.Properties()));
    public static final RegistryObject<Item> SCARAB_GOLD = ITEMS.register("scarab_gold", () -> new ItemSetting(new Item.Properties()));
    public static final RegistryObject<Item> SCARAB_AURITEH = ITEMS.register("scarab_auriteh", () -> new ItemSetting(new Item.Properties()));
    public static final RegistryObject<Item> SCARAB_LAZATEP = ITEMS.register("scarab_lazotep", () -> new ItemSetting(new Item.Properties()));
    public static final RegistryObject<Item> AROMATIC_OIL_JUNIPER_TREE = ITEMS.register("aromatic_oil_juniper_tree",
            () -> new ItemSetting(new Item.Properties(), Component.translatable("item." + ThirteenFlames.MODID + ".aromatic_oil_juniper_tree.tooltip")));
    public static final RegistryObject<Item> AROMATIC_OIL_ROSE = ITEMS.register("aromatic_oil_rose",
            () -> new ItemSetting(new Item.Properties(), Component.translatable("item." + ThirteenFlames.MODID + ".aromatic_oil_rose.tooltip")));
    public static final RegistryObject<Item> AROMATIC_OIL_ROSEWOOD_MINT = ITEMS.register("aromatic_oil_rosewood_mint",
            () -> new ItemSetting(new Item.Properties(), Component.translatable("item." + ThirteenFlames.MODID + ".aromatic_oil_rosewood_mint.tooltip")));
    public static final RegistryObject<Item> AROMATIC_OIL_LILA_GOOSEBERRY = ITEMS.register("aromatic_oil_lila_gooseberry",
            () -> new ItemSetting(new Item.Properties(), Component.translatable("item." + ThirteenFlames.MODID + ".aromatic_oil_lila_gooseberry.tooltip")));
    public static final RegistryObject<Item> RECORD = ITEMS.register("record", () -> new RecordItem(6, SoundsRegistry.RECORD,
            new Item.Properties().tab(ThirteenFlames.ITEMS_TAB).stacksTo(1), 3740));

    public static final RegistryObject<Item> MASK_SALMANA = ITEMS.register("mask_salmana",
            () -> new MaskSalmana(ArmorMaterials.NETHERITE, EquipmentSlot.HEAD,
                    new Item.Properties().tab(ThirteenFlames.ITEMS_TAB).defaultDurability(-1).durability(-1)));
    public static final RegistryObject<Item> MASK_TOIFETUN_SILVER = ITEMS.register("mask_toifetun_silver",
            () -> new ArmorItem(ArmorMaterials.NETHERITE, EquipmentSlot.HEAD,
                    new Item.Properties().tab(ThirteenFlames.ITEMS_TAB).durability(-1).defaultDurability(-1)));
    public static final RegistryObject<Item> MASK_TOIFETUN_GOLD = ITEMS.register("mask_toifetun_gold",
            () -> new ArmorItem(ArmorMaterials.NETHERITE, EquipmentSlot.HEAD,
                    new Item.Properties().tab(ThirteenFlames.ITEMS_TAB).durability(-1).defaultDurability(-1)));
    public static final RegistryObject<Item> MASK_TOIFETUN_WHITE = ITEMS.register("mask_toifetun_white",
            () -> new ArmorItem(ArmorMaterials.NETHERITE, EquipmentSlot.HEAD,
                    new Item.Properties().tab(ThirteenFlames.ITEMS_TAB).durability(-1).defaultDurability(-1)));

    public static void register() {
        ITEMS.register( FMLJavaModLoadingContext.get().getModEventBus());
    }
}
