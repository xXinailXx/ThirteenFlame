package net.xXinailXx.thirteen_flames.item;

import com.mojang.authlib.GameProfile;
import it.hurts.sskirillss.relics.utils.NBTUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.xXinailXx.thirteen_flames.init.ItemRegistry;
import net.xXinailXx.thirteen_flames.item.base.ItemSetting;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;

public class NarsafarItem extends ItemSetting {
    private GameProfile owner = Minecraft.getInstance().getUser().getGameProfile();

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        Hover hover = getHover(stack);
        tooltip.add(Component.translatable("item.thirteen_flames.narsafar.name.tooltip", hover.name()).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.thirteen_flames.narsafar.gender.tooltip", hover.gender()).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.thirteen_flames.narsafar.profession.tooltip", hover.profession()).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.thirteen_flames.narsafar.owner.tooltip", hover.owner()).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.thirteen_flames.narsafar.number.tooltip", hover.series(), hover.series_str()).withStyle(ChatFormatting.GRAY));
    }

    public static ItemStack createNarsafar(String name, String gender, String profession, String owner, int series, String series_str) {
        if (series < 0 || series > 999)
            throw new IllegalArgumentException();

        ItemStack stack = ItemRegistry.NARSAFAR.get().getDefaultInstance();

        CompoundTag tag = stack.getOrCreateTag();
        tag.putString("hover_name", name);
        tag.putString("hover_gender", gender);
        tag.putString("hover_profession", profession);
        tag.putString("hover_owner", owner);
        tag.putString("hover_series_int", series < 10 ? "00" + series : series < 100 ? "0" + series : String.valueOf(series));
        tag.putString("hover_series_str", series_str);
        stack.setTag(tag);

        return stack;
    }

    private Hover getHover(ItemStack stack) {
        String name = NBTUtils.getString(stack, "hover_name", "none");
        String gender = NBTUtils.getString(stack, "hover_gender", "none");
        String profession = NBTUtils.getString(stack, "hover_profession", "none");
        String owner = NBTUtils.getString(stack, "hover_owner", "none");
        String series = NBTUtils.getString(stack, "hover_series_int", "none");
        String series_str = NBTUtils.getString(stack, "hover_series_str", "none");

        return new Hover(name, gender, profession, owner, series, series_str);
    }

    private record Hover(String name, String gender, String profession, String owner, String series, String series_str) {}
}
