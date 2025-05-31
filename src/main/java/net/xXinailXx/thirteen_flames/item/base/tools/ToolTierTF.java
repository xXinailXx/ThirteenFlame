package net.xXinailXx.thirteen_flames.item.base.tools;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.TierSortingRegistry;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.init.ItemRegistry;
import net.xXinailXx.thirteen_flames.utils.ThirteenFlamesTags;

import java.util.List;

public class ToolTierTF {
    public static Tier THIRTEEN_FLAMES = TierSortingRegistry.registerTier(new ForgeTier(4, 2031, 9.0F, 3.0F, 15,
            ThirteenFlamesTags.Blocks.NEEDS_FLAME_TOOL, () -> Ingredient.of(ItemRegistry.LAZOTEP_INGOT.get())),
            new ResourceLocation(ThirteenFlames.MODID, "flame"), List.of(Tiers.NETHERITE), List.of());
}
