package net.xXinailXx.thirteen_flames.item;

import com.mojang.authlib.GameProfile;
import it.hurts.sskirillss.relics.utils.NBTUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.xXinailXx.thirteen_flames.config.ThirteenFlamesConfig;
import net.xXinailXx.thirteen_flames.utils.ItemSetting;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Narsafar extends ItemSetting {
    private GameProfile owner = Minecraft.getInstance().getUser().getGameProfile();

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.thirteen_flames.narsafar.name.tooltip", String.valueOf(this.owner.getName())).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.thirteen_flames.narsafar.gender.tooltip", ThirteenFlamesConfig.NARSAFAR_GENDER.get()).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.thirteen_flames.narsafar.profession.tooltip", ThirteenFlamesConfig.NARSAFAR_PROFFESION.get()).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.thirteen_flames.narsafar.owner.tooltip", ThirteenFlamesConfig.NARSAFAR_OWNER.get()).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.thirteen_flames.narsafar.number.tooltip", ThirteenFlamesConfig.NARSAFAR_SERIES_INT.get(), ThirteenFlamesConfig.NARSAFAR_SERIES_STRING.get()).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal(" "));
        tooltip.add(Component.translatable("item.thirteen_flames.narsafar.config.tooltip").withStyle(ChatFormatting.GRAY));
    }
}
