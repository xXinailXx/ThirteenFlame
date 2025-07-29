package net.xXinailXx.thirteen_flames.client.gui.scroll;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public class EnchantmentUtils {
    public static Map<Enchantment, Ench> getPossibleEnch(ItemStack stack) {
        Map<Enchantment, EnchantmentUtils.Ench> map = new HashMap<>();

        for (Enchantment ench : ForgeRegistries.ENCHANTMENTS.getValues()) {
            if (ench.canEnchant(stack)) {
                int enchLevel = stack.getEnchantmentLevel(ench);

                if (enchLevel == 0)
                    map.put(ench, new Ench(ench, 1, ench.getMaxLevel()));
                else if (enchLevel < ench.getMaxLevel())
                    map.put(ench, new Ench(ench, enchLevel, ench.getMaxLevel()));
            }
        }

        return map;
    }

    @Getter
    @Setter
    public static class Ench {
        private Enchantment ench;
        private int minLevel;
        private int maxLevel;

        public Ench(Enchantment ench, int minLevel, int maxLevel) {
            this.ench = ench;
            this.minLevel = minLevel;
            this.maxLevel = maxLevel;
        }
    }
}
