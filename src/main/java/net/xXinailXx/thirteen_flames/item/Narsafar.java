package net.xXinailXx.thirteen_flames.item;

import com.mojang.authlib.GameProfile;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.xXinailXx.thirteen_flames.utils.ItemSetting;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Narsafar extends ItemSetting {
    private GameProfile owner = Minecraft.getInstance().getUser().getGameProfile();

    public Narsafar(Properties properties) {
        super(properties);
    }

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.st_thirteen_lights.narsafar.name.tooltip", String.valueOf( this.owner )).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("tooltip.st_thirteen_lights.narsafar.gender.tooltip", Component.literal("d")).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("tooltip.st_thirteen_lights.narsafar.profession.tooltip", Component.literal("d")).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("tooltip.st_thirteen_lights.narsafar.owner.tooltip", Component.literal("d")).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("tooltip.st_thirteen_lights.narsafar.number.tooltip", Component.literal("d"), Component.literal("d")).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal(" "));
        tooltip.add(Component.translatable("tooltip.st_thirteen_lights.narsafar.info_in_config.tooltip").withStyle(ChatFormatting.GRAY));
    }
}
