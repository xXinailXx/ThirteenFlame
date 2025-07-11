package net.xXinailXx.thirteen_flames.client.progress;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.json.JsonExecutor;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.ForgeRegistries;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import oshi.util.tuples.Pair;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ProgressHelper {
    public static List<ItemProgress> ITEMS_REQ_PROGRESS = new ArrayList();

    public static void read() {
        String fileName = "items_req_progress.json";
        File file = new File("config" + File.separator + ThirteenFlames.MODID);

        File f = new File(file.getPath(), fileName);

        if (!f.exists())
            write();

        JsonElement element = JsonExecutor.read(file.toPath(), fileName);

        for (Map.Entry<String, JsonElement> e : element.getAsJsonObject().entrySet()) {
            JsonObject obj = e.getValue().getAsJsonObject();

            List<Pair<ProgressManager.ProgressType, Integer>> itemReqP = new ArrayList<>();

            for (String pKey : obj.keySet()) {
                int reqLevel = obj.get(pKey).getAsInt();

                itemReqP.add(new Pair<>(ProgressManager.ProgressType.valueOf(pKey.toUpperCase(Locale.ROOT)), reqLevel));
            }

            String modId = e.getKey().substring(0, e.getKey().indexOf(":"));
            String itemName = e.getKey().replace(modId, "").replace(":", "");

            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(modId, itemName));

            ITEMS_REQ_PROGRESS.add(new ItemProgress(modId, item, itemReqP));
        }
    }

    public static void write() {
        String fileName = "items_req_progress.json";
        File file = new File("config" + File.separator + ThirteenFlames.MODID);

        JsonObject obj = new JsonObject();

        for (ItemProgress progress : writeDefualtData()) {
            JsonObject object = new JsonObject();

            for (Pair<ProgressManager.ProgressType, Integer> pair : progress.itemReqP())
                object.addProperty(pair.getA().name().toLowerCase(Locale.ROOT), pair.getB());

            obj.add(progress.modId + ":" + Registry.ITEM.getKey(progress.item()).getPath(), object);
        }

        File f = new File(file.getPath(), fileName);

        try {
            file.mkdirs();
            f.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JsonExecutor.write(file.toPath(), fileName, obj);
    }

    private static List<ItemProgress> writeDefualtData() {
        List<ItemProgress> list = new ArrayList<>();

        for (Item item : ForgeRegistries.ITEMS.getValues()) {
            if (!Registry.ITEM.getKey(item).getNamespace().equals("minecraft"))
                continue;

            ItemStack stack = item.getDefaultInstance();
            List<Pair<ProgressManager.ProgressType, Integer>> itemReqP = new ArrayList<>();

            if (item instanceof TieredItem tier) {
                if (item instanceof SwordItem) {
                    if (tier.getTier().equals(Tiers.STONE))
                        itemReqP.add(new Pair<>(ProgressManager.ProgressType.FIGHT, 10));
                    else if (tier.getTier().equals(Tiers.GOLD))
                        itemReqP.add(new Pair<>(ProgressManager.ProgressType.FIGHT, 20));
                    else if (tier.getTier().equals(Tiers.IRON))
                        itemReqP.add(new Pair<>(ProgressManager.ProgressType.FIGHT, 30));
                    else if (tier.getTier().equals(Tiers.DIAMOND))
                        itemReqP.add(new Pair<>(ProgressManager.ProgressType.FIGHT, 40));
                    else if (tier.getTier().equals(Tiers.NETHERITE))
                        itemReqP.add(new Pair<>(ProgressManager.ProgressType.FIGHT, 50));
                } else if (item instanceof PickaxeItem) {
                    if (tier.getTier().equals(Tiers.STONE))
                        itemReqP.add(new Pair<>(ProgressManager.ProgressType.MINING, 10));
                    else if (tier.getTier().equals(Tiers.GOLD))
                        itemReqP.add(new Pair<>(ProgressManager.ProgressType.MINING, 20));
                    else if (tier.getTier().equals(Tiers.IRON))
                        itemReqP.add(new Pair<>(ProgressManager.ProgressType.MINING, 30));
                    else if (tier.getTier().equals(Tiers.DIAMOND))
                        itemReqP.add(new Pair<>(ProgressManager.ProgressType.MINING, 40));
                    else if (tier.getTier().equals(Tiers.NETHERITE))
                        itemReqP.add(new Pair<>(ProgressManager.ProgressType.MINING, 50));
                } else if (item instanceof HoeItem) {
                    if (tier.getTier().equals(Tiers.STONE))
                        itemReqP.add(new Pair<>(ProgressManager.ProgressType.CRAFT, 10));
                    else if (tier.getTier().equals(Tiers.GOLD))
                        itemReqP.add(new Pair<>(ProgressManager.ProgressType.CRAFT, 20));
                    else if (tier.getTier().equals(Tiers.IRON))
                        itemReqP.add(new Pair<>(ProgressManager.ProgressType.CRAFT, 30));
                    else if (tier.getTier().equals(Tiers.DIAMOND))
                        itemReqP.add(new Pair<>(ProgressManager.ProgressType.CRAFT, 40));
                    else if (tier.getTier().equals(Tiers.NETHERITE))
                        itemReqP.add(new Pair<>(ProgressManager.ProgressType.CRAFT, 50));
                } else if (item instanceof AxeItem) {
                    if (tier.getTier().equals(Tiers.STONE)) {
                        itemReqP.add(new Pair<>(ProgressManager.ProgressType.MINING, 10));
                        itemReqP.add(new Pair<>(ProgressManager.ProgressType.FIGHT, 40));
                    } else if (tier.getTier().equals(Tiers.GOLD)) {
                        itemReqP.add(new Pair<>(ProgressManager.ProgressType.MINING, 20));
                        itemReqP.add(new Pair<>(ProgressManager.ProgressType.FIGHT, 40));
                    } else if (tier.getTier().equals(Tiers.IRON)) {
                        itemReqP.add(new Pair<>(ProgressManager.ProgressType.MINING, 30));
                        itemReqP.add(new Pair<>(ProgressManager.ProgressType.FIGHT, 45));
                    } else if (tier.getTier().equals(Tiers.DIAMOND)) {
                        itemReqP.add(new Pair<>(ProgressManager.ProgressType.MINING, 40));
                        itemReqP.add(new Pair<>(ProgressManager.ProgressType.FIGHT, 50));
                    } else if (tier.getTier().equals(Tiers.NETHERITE)) {
                        itemReqP.add(new Pair<>(ProgressManager.ProgressType.MINING, 50));
                        itemReqP.add(new Pair<>(ProgressManager.ProgressType.FIGHT, 60));
                    }
                } else if (item instanceof ShovelItem) {
                    if (tier.getTier().equals(Tiers.STONE)) {
                        itemReqP.add(new Pair<>(ProgressManager.ProgressType.MINING, 10));
                        itemReqP.add(new Pair<>(ProgressManager.ProgressType.CRAFT, 15));
                    } else if (tier.getTier().equals(Tiers.GOLD)) {
                        itemReqP.add(new Pair<>(ProgressManager.ProgressType.MINING, 20));
                        itemReqP.add(new Pair<>(ProgressManager.ProgressType.CRAFT, 20));
                    } else if (tier.getTier().equals(Tiers.IRON)) {
                        itemReqP.add(new Pair<>(ProgressManager.ProgressType.MINING, 30));
                        itemReqP.add(new Pair<>(ProgressManager.ProgressType.CRAFT, 25));
                    } else if (tier.getTier().equals(Tiers.DIAMOND)) {
                        itemReqP.add(new Pair<>(ProgressManager.ProgressType.MINING, 40));
                        itemReqP.add(new Pair<>(ProgressManager.ProgressType.CRAFT, 35));
                    } else if (tier.getTier().equals(Tiers.NETHERITE)) {
                        itemReqP.add(new Pair<>(ProgressManager.ProgressType.MINING, 50));
                        itemReqP.add(new Pair<>(ProgressManager.ProgressType.CRAFT, 45));
                    }
                }
            } else if (item instanceof ArmorItem armor) {
                if (armor.getMaterial().equals(ArmorMaterials.LEATHER))
                    itemReqP.add(new Pair<>(ProgressManager.ProgressType.FIGHT, 10));
                else if (armor.getMaterial().equals(ArmorMaterials.CHAIN))
                    itemReqP.add(new Pair<>(ProgressManager.ProgressType.FIGHT, 20));
                else if (armor.getMaterial().equals(ArmorMaterials.GOLD))
                    itemReqP.add(new Pair<>(ProgressManager.ProgressType.FIGHT, 20));
                else if (armor.getMaterial().equals(ArmorMaterials.IRON))
                    itemReqP.add(new Pair<>(ProgressManager.ProgressType.FIGHT, 40));
                else if (armor.getMaterial().equals(ArmorMaterials.DIAMOND))
                    itemReqP.add(new Pair<>(ProgressManager.ProgressType.FIGHT, 50));
                else if (armor.getMaterial().equals(ArmorMaterials.NETHERITE))
                    itemReqP.add(new Pair<>(ProgressManager.ProgressType.FIGHT, 65));
                else
                    itemReqP.add(new Pair<>(ProgressManager.ProgressType.FIGHT, 5));
            } else if (stack.is(Items.BOW)) {
                itemReqP.add(new Pair<>(ProgressManager.ProgressType.CRAFT, 20));
                itemReqP.add(new Pair<>(ProgressManager.ProgressType.FIGHT, 20));
            } else if (stack.is(Items.FLINT_AND_STEEL)) {
                itemReqP.add(new Pair<>(ProgressManager.ProgressType.CRAFT, 10));
            } else if (stack.is(Items.ENCHANTED_GOLDEN_APPLE)) {
                itemReqP.add(new Pair<>(ProgressManager.ProgressType.HEALTH, 70));
            } else if (stack.is(Items.GOLDEN_APPLE)) {
                itemReqP.add(new Pair<>(ProgressManager.ProgressType.HEALTH, 50));
            } else if (stack.is(Items.MUSHROOM_STEW)) {
                itemReqP.add(new Pair<>(ProgressManager.ProgressType.HEALTH, 15));
            } else if (stack.is(Items.COOKED_BEEF)) {
                itemReqP.add(new Pair<>(ProgressManager.ProgressType.HEALTH, 25));
            } else if (stack.is(Items.COOKED_CHICKEN)) {
                itemReqP.add(new Pair<>(ProgressManager.ProgressType.HEALTH, 25));
            } else if (stack.is(Items.COOKED_COD)) {
                itemReqP.add(new Pair<>(ProgressManager.ProgressType.HEALTH, 25));
            } else if (stack.is(Items.COOKED_MUTTON)) {
                itemReqP.add(new Pair<>(ProgressManager.ProgressType.HEALTH, 25));
            } else if (stack.is(Items.COOKED_RABBIT)) {
                itemReqP.add(new Pair<>(ProgressManager.ProgressType.HEALTH, 25));
            } else if (stack.is(Items.COOKED_PORKCHOP)) {
                itemReqP.add(new Pair<>(ProgressManager.ProgressType.HEALTH, 25));
            } else if (stack.is(Items.COOKED_SALMON)) {
                itemReqP.add(new Pair<>(ProgressManager.ProgressType.HEALTH, 25));
            } else if (stack.is(Items.RABBIT_STEW)) {
                itemReqP.add(new Pair<>(ProgressManager.ProgressType.HEALTH, 35));
            } else if (stack.is(Items.ELYTRA)) {
                itemReqP.add(new Pair<>(ProgressManager.ProgressType.CRAFT, 65));
            } else if (stack.is(Items.CROSSBOW)) {
                itemReqP.add(new Pair<>(ProgressManager.ProgressType.CRAFT, 40));
                itemReqP.add(new Pair<>(ProgressManager.ProgressType.CRAFT, 40));
            } else if (stack.is(Items.TRIDENT)) {
                itemReqP.add(new Pair<>(ProgressManager.ProgressType.CRAFT, 50));
            } else if (stack.is(Items.SHIELD)) {
                itemReqP.add(new Pair<>(ProgressManager.ProgressType.CRAFT, 25));
                itemReqP.add(new Pair<>(ProgressManager.ProgressType.FIGHT, 25));
            } else if (stack.is(Items.TOTEM_OF_UNDYING)) {
                itemReqP.add(new Pair<>(ProgressManager.ProgressType.CRAFT, 70));
                itemReqP.add(new Pair<>(ProgressManager.ProgressType.HEALTH, 70));
            } else {
                continue;
            }

            list.add(new ItemProgress("minecraft", item, itemReqP));
        }

        return list;
    }

    public record ItemProgress(String modId, Item item, List<Pair<ProgressManager.ProgressType, Integer>> itemReqP) {}
}
