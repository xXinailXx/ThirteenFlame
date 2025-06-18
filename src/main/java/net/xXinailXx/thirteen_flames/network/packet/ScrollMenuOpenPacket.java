package net.xXinailXx.thirteen_flames.network.packet;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;
import net.xXinailXx.thirteen_flames.client.gui.scroll.EnchantmentUtils;
import net.xXinailXx.thirteen_flames.client.gui.scroll.ScrollMenuProvider;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.PacketContext;
import oshi.util.tuples.Pair;

import java.util.HashMap;
import java.util.Map;

public class ScrollMenuOpenPacket implements IPacket {
    private ItemStack stack;
    private int action;
    private int enchLevel;
    private int scrollY0;
    private int scrollY1;
    private EnchantmentUtils.Ench ench;
    private Map<Enchantment, Integer> enchs;
    private Map<Enchantment, EnchantmentUtils.Ench> possibleEnchs;
    private ItemStack slotStack;

    public ScrollMenuOpenPacket(ItemStack stack, int action, int enchLevel, int scrollY0, int scrollY1, EnchantmentUtils.Ench ench, ItemStack slotStack, Map<Enchantment, Integer> enchs, Map<Enchantment, EnchantmentUtils.Ench> possibleEnchs) {
        this.stack = stack;
        this.action = action;
        this.enchLevel = enchLevel;
        this.ench = ench;
        this.enchs = enchs;
        this.scrollY0 = scrollY0;
        this.scrollY1 = scrollY1;
        this.possibleEnchs = possibleEnchs;
        this.slotStack = slotStack;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeItem(this.stack);
        buf.writeInt(this.action);
        buf.writeInt(this.enchLevel);
        buf.writeInt(this.scrollY0);
        buf.writeInt(this.scrollY1);
        buf.writeItem(this.slotStack);
        buf.writeNbt(writeEnch(this.ench));
        buf.writeNbt(writeEnchs(this.enchs));
        buf.writeNbt(writePossibleEnchs(this.possibleEnchs));
    }

    public void read(FriendlyByteBuf buf) {
        this.stack = buf.readItem();
        this.action = buf.readInt();
        this.enchLevel = buf.readInt();
        this.scrollY0 = buf.readInt();
        this.scrollY1 = buf.readInt();
        this.slotStack = buf.readItem();
        this.ench = readEnch(buf.readNbt());
        this.enchs = readEnchs(buf.readNbt());
        this.possibleEnchs = readPossibleEnchs(buf.readNbt());
    }

    private CompoundTag writeEnch(EnchantmentUtils.Ench ench) {
        CompoundTag tag = new CompoundTag();

        if (ench != null) {
            ResourceLocation location = ForgeRegistries.ENCHANTMENTS.getKey(ench.getEnch());

            tag.putString("ench_namespace", location.getNamespace());
            tag.putString("ench_path", location.getPath());
            tag.putInt("ench_min_level", ench.getMinLevel());
            tag.putInt("ench_max_level", ench.getMaxLevel());
        }

        return tag;
    }

    private CompoundTag writeEnchs(Map<Enchantment, Integer> enchs) {
        CompoundTag tag = new CompoundTag();

        if (!enchs.isEmpty()) {
            int count = 0;

            for (Enchantment ench : enchs.keySet()) {
                CompoundTag tag1 = new CompoundTag();
                int enchLevel = enchs.get(ench);

                ResourceLocation location = ForgeRegistries.ENCHANTMENTS.getKey(ench);

                tag1.putString("ench_namespace", location.getNamespace());
                tag1.putString("ench_path", location.getPath());
                tag1.putInt("ench_level", enchLevel);
                tag.put("ench_c_" + count, tag1);

                count++;
            }

            tag.putInt("enchs_count", count);
        }

        return tag;
    }

    private CompoundTag writePossibleEnchs(Map<Enchantment, EnchantmentUtils.Ench> possibleEnchs) {
        CompoundTag tag = new CompoundTag();

        if (!possibleEnchs.isEmpty()) {
            int count = 0;

            for (Enchantment ench : possibleEnchs.keySet()) {
                EnchantmentUtils.Ench ench1 = possibleEnchs.get(ench);
                CompoundTag tag1 = new CompoundTag();
                ResourceLocation location = ForgeRegistries.ENCHANTMENTS.getKey(ench);

                tag1.putString("ench_namespace", location.getNamespace());
                tag1.putString("ench_path", location.getPath());
                tag1.put("ench_nbt", writeEnch(ench1));
                tag.put("ench_c_" + count, tag1);

                count++;
            }

            tag.putInt("enchs_count", count);
        }

        return tag;
    }

    private EnchantmentUtils.Ench readEnch(CompoundTag tag) {
        if (tag.isEmpty()) {
            return null;
        } else {
            ResourceLocation location = new ResourceLocation(tag.getString("ench_namespace"), tag.getString("ench_path"));

            int minLevel = tag.getInt("ench_min_level");
            int maxLevel = tag.getInt("ench_max_level");

            return new EnchantmentUtils.Ench(ForgeRegistries.ENCHANTMENTS.getValue(location), minLevel, maxLevel);
        }
    }

    private Map<Enchantment, Integer> readEnchs(CompoundTag tag) {
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
    }

    private Map<Enchantment, EnchantmentUtils.Ench> readPossibleEnchs(CompoundTag tag) {
        if (tag.isEmpty())
            return new HashMap<>();

        Map<Enchantment, EnchantmentUtils.Ench> enchs = new HashMap<>();
        int count = tag.getInt("enchs_count");

        for (int i = 0; i < count; i++) {
            CompoundTag tag1 = tag.getCompound("ench_c_" + i);

            ResourceLocation location = new ResourceLocation(tag1.getString("ench_namespace"), tag1.getString("ench_path"));
            int enchLevel = tag1.getInt("ench_level");
            enchs.put(ForgeRegistries.ENCHANTMENTS.getValue(location), readEnch(tag1.getCompound("ench_nbt")));
        }

        return enchs;
    }

    public void serverExecute(PacketContext ctx) {
        ServerPlayer player = ctx.getSender();

        if (player == null)
            return;

        NetworkHooks.openScreen(player, new ScrollMenuProvider(this.stack, this.slotStack, this.action, this.enchLevel, this.scrollY0, this.scrollY1, this.ench, this.possibleEnchs, this.enchs), buf -> {
            buf.writeItem(this.stack);
            buf.writeItem(this.slotStack);
            buf.writeInt(this.action);
            buf.writeInt(this.enchLevel);
            buf.writeInt(this.scrollY0);
            buf.writeInt(this.scrollY1);
            buf.writeNbt(writeEnch(this.ench));
            buf.writeNbt(writeEnchs(this.enchs));
            buf.writeNbt(writePossibleEnchs(this.possibleEnchs));
        });
    }
}
