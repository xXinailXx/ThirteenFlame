package net.xXinailXx.thirteen_flames.client.gui.scroll;

import com.google.common.base.Suppliers;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.Nullable;
import oshi.util.tuples.Pair;

import java.util.Map;
import java.util.function.Supplier;

public class ScrollMenuProvider implements MenuProvider {
    private final ItemStack stack;
    private final int action;
    private final int enchLevel;
    private final int scrollY0;
    private final int scrollY1;
    private final EnchantmentUtils.Ench ench;
    private final Map<Enchantment, EnchantmentUtils.Ench> possibleEnchs;
    private final Map<Enchantment, Integer> enchs;
    private final ItemStack slotStack;

    public ScrollMenuProvider(ItemStack stack, ItemStack slotStack, int action, int enchLevel, int scrollY0, int scrollY1, EnchantmentUtils.Ench ench, Map<Enchantment, EnchantmentUtils.Ench> possibleEnchs, Map<Enchantment, Integer> enchs) {
        this.stack = stack;
        this.action = action;
        this.enchLevel = enchLevel;
        this.scrollY0 = scrollY0;
        this.scrollY1 = scrollY1;
        this.ench = ench;
        this.possibleEnchs = possibleEnchs;
        this.enchs = enchs;
        this.slotStack = slotStack;
    }

    public Component getDisplayName() {
        return Component.empty();
    }

    public @Nullable AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new ScrollMenu(id, inventory, this.stack, this.slotStack, this.action, this.enchLevel, this.scrollY0, this.scrollY1, Suppliers.memoize(() -> this.ench), Suppliers.memoize(() -> this.enchs), Suppliers.memoize(() -> this.possibleEnchs));
    }
}
