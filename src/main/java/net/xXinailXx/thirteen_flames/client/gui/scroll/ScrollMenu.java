package net.xXinailXx.thirteen_flames.client.gui.scroll;

import it.hurts.sskirillss.relics.utils.NBTUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;
import net.xXinailXx.thirteen_flames.init.MenuRegistry;
import net.xXinailXx.thirteen_flames.network.packet.ScrollMenuOpenPacket;
import org.zeith.hammerlib.net.Network;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ScrollMenu extends AbstractContainerMenu {
    public ItemStack scrollStack;
    public final int scrollType;
    public int action;
    public final int scroll0;
    public final int scroll1;
    public final EnchantmentUtils.Ench ench;
    public final Map<Enchantment, Integer> enchs;
    public final Map<Enchantment, EnchantmentUtils.Ench> possibleEnchs;
    public ItemStack stack;
    private final Slot slot;
    public final int enchLevel;
    public boolean rebuild = false;

    public ScrollMenu(int id, Inventory inventory, ItemStack scrollStack, ItemStack slotStack, int action, int enchLevel, int scroll0, int scroll1, Supplier<EnchantmentUtils.Ench> ench, Supplier<Map<Enchantment, Integer>> enchs, Supplier<Map<Enchantment, EnchantmentUtils.Ench>> possibleEnchs) {
        super(MenuRegistry.SCROLL_MENU.get(), id);
        this.scrollStack = scrollStack;
        this.stack = slotStack;
        this.scrollType = NBTUtils.getInt(scrollStack, "type", 0);
        this.action = action;
        this.scroll0 = scroll0;
        this.scroll1 = scroll1;
        this.ench = ench.get();
        this.enchs = enchs.get();
        this.enchLevel = enchLevel;
        this.possibleEnchs = possibleEnchs.get();

        int x = (Minecraft.getInstance().getWindow().getGuiScaledWidth() - 331) / 2;
        int y = (Minecraft.getInstance().getWindow().getGuiScaledHeight() - 249) / 2;

        Container container = new SimpleContainer(1) {
            public void setChanged() {
                super.setChanged();
                ScrollMenu.this.slotsChanged(this);
            }
        };
        this.slot = this.addSlot(new Slot(container, 0, x + 157, y + 28));
        this.slot.set(slotStack);

        for (int i = 0; i < 3; ++i)
            for (int j = 0; j < 9; ++j)
                this.addSlot(new Slot(inventory, j + i * 9 + 9, x + 85 + j * 18, y + 167 + i * 18));

        for (int i = 0; i < 9; ++i)
            this.addSlot(new Slot(inventory, i, x + 85 + i * 18, y + 225));
    }

    public ScrollMenu(int id, Inventory inventory, FriendlyByteBuf buf) {
        this(id, inventory, buf.readItem(), buf.readItem(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt(), () -> {
            CompoundTag tag = buf.readNbt();

            if (tag.isEmpty()) {
                return null;
            } else {
                ResourceLocation location = new ResourceLocation(tag.getString("ench_namespace"), tag.getString("ench_path"));

                int minLevel = tag.getInt("ench_min_level");
                int maxLevel = tag.getInt("ench_max_level");

                return new EnchantmentUtils.Ench(ForgeRegistries.ENCHANTMENTS.getValue(location), minLevel, maxLevel);
            }
        }, () -> {
            CompoundTag tag = buf.readNbt();

            if (tag.isEmpty())
                return new HashMap<>();

            Map<Enchantment, Integer> enchs = new HashMap<>();
            int count = tag.getInt("enchs_count");

            for (int i = 0; i < count; i++) {
                CompoundTag tag1 = tag.getCompound("ench_c_" + i);

                ResourceLocation location = new ResourceLocation(tag1.getString("ench_namespace"), tag1.getString("ench_path"));
                int enchLevel = tag1.getInt("ench_level");
                enchs.put(ForgeRegistries.ENCHANTMENTS.getValue(location), enchLevel);
            }

            return enchs;
        }, () -> {
            CompoundTag tag = buf.readNbt();

            Map<Enchantment, EnchantmentUtils.Ench> enchs = new HashMap<>();
            int count = tag.getInt("enchs_count");

            for (int i = 0; i < count; i++) {
                CompoundTag tag1 = tag.getCompound("ench_c_" + i);

                ResourceLocation location = new ResourceLocation(tag1.getString("ench_namespace"), tag1.getString("ench_path"));
                EnchantmentUtils.Ench ench1;

                CompoundTag tag2 = tag1.getCompound("ench_nbt");

                if (tag2.isEmpty()) {
                    return null;
                } else {
                    int minLevel = tag2.getInt("ench_min_level");
                    int maxLevel = tag2.getInt("ench_max_level");

                    ench1 = new EnchantmentUtils.Ench(ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(tag2.getString("ench_namespace"), tag2.getString("ench_path"))), minLevel, maxLevel);
                }

                enchs.put(ForgeRegistries.ENCHANTMENTS.getValue(location), ench1);
            }

            return enchs;
        });
    }

    public void tick() {
        if (this.stack.isEmpty() && this.slot.hasItem()) {
            Network.sendToServer(new ScrollMenuOpenPacket(this.scrollStack, -1, 0, 0, 0, null, this.slot.getItem(), new HashMap<>(), EnchantmentUtils.getPossibleEnch(this.slot.getItem())));
        } else if (!this.stack.equals(this.slot.getItem(), false)) {
            this.stack = this.slot.getItem();
            this.rebuild = true;
        } else if (!this.stack.isEmpty() && !this.slot.hasItem()) {
            Network.sendToServer(new ScrollMenuOpenPacket(this.scrollStack, -1, 0, 0, 0, null, ItemStack.EMPTY, new HashMap<>(), new HashMap<>()));
        }
    }

    public ItemStack quickMoveStack(Player player, int index) {
        Slot sourceSlot = slots.get(index);

        if (!sourceSlot.hasItem())
            return ItemStack.EMPTY;

        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        if (index == 0) {
            if (!moveItemStackTo(sourceStack, 1, 37, false))
                return ItemStack.EMPTY;
        } else {
            if (!this.slot.hasItem()) {
                this.slot.set(sourceStack);
                sourceSlot.set(ItemStack.EMPTY);
            } else {
                if (!moveItemStackTo(sourceStack, 1, 37, false))
                    return ItemStack.EMPTY;
            }
        }

        if (sourceStack.getCount() == 0)
            sourceSlot.set(ItemStack.EMPTY);
        else
            sourceSlot.setChanged();

        sourceSlot.onTake(player, sourceStack);

        return copyOfSourceStack;
    }

    public boolean stillValid(Player player) {
        return true;
    }
}
