package net.xXinailXx.thirteen_flames.client.gui.button.abilities.global;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mod.EventBusSubscriber
public class Heat extends AbstarctAbilityWidgets {
    public Heat(int x, int y) {
        super(x, y, 1);
    }

    @Override
    public AbilityData constructAbilityData() {
        return AbilityData.builder("heat").screenID(ScreenID.GLOBAL).build();
    }

    @SubscribeEvent
    public static void meltingItem(TickEvent.PlayerTickEvent event) {
        if (data.isActiveAbility("heat")) {
            Player player = event.player;

            if (player == null) {
                return;
            }

            ItemStack stack = player.getOffhandItem();
            if (!stack.is(Items.AIR)) {
                Item item = stack.getItem();

                if (item instanceof TieredItem tieredItem) {
                    Tiers tiers = (Tiers) tieredItem.getTier();

                    switch (tiers) {
                        case WOOD -> player.setItemSlot(EquipmentSlot.OFFHAND, Items.CHARCOAL.getDefaultInstance());
                        case STONE -> player.setItemSlot(EquipmentSlot.OFFHAND, Items.COBBLESTONE.getDefaultInstance());
                        case IRON -> player.setItemSlot(EquipmentSlot.OFFHAND, Items.IRON_NUGGET.getDefaultInstance());
                        case DIAMOND -> player.setItemSlot(EquipmentSlot.OFFHAND, Items.DIAMOND.getDefaultInstance());
                        case GOLD -> player.setItemSlot(EquipmentSlot.OFFHAND, Items.GOLD_NUGGET.getDefaultInstance());
                        case NETHERITE -> player.setItemSlot(EquipmentSlot.OFFHAND, Items.NETHERITE_SCRAP.getDefaultInstance());
                        default -> player.setItemSlot(EquipmentSlot.OFFHAND, tiers.getRepairIngredient().getItems()[0]);
                    }
                } else {
                    if (stack.getFoodProperties(player) != null) {

                    }

                    RecipeManager recipeManager = player.getLevel().getRecipeManager();

                    List<SmeltingRecipe> smeltingRecipes = recipeManager.getRecipesFor(RecipeType.SMELTING, new SimpleContainer(stack), player.getLevel());

                    smeltingRecipes.forEach(recipe -> {
                        for (Ingredient ingredient : recipe.getIngredients()) {
                            if (ingredient.test(stack)) {
                                player.setItemSlot(EquipmentSlot.OFFHAND, recipe.getResultItem());
                                return;
                            }
                        }
                    });
                }
            }
        }
    }
}
