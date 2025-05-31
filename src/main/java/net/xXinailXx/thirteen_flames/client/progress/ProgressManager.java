package net.xXinailXx.thirteen_flames.client.progress;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.data.Data;
import net.xXinailXx.thirteen_flames.data.IData;
import org.jetbrains.annotations.Nullable;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProgressManager {
    private static final IData.IGuiLevelingData data = new Data.GuiLevelingData();

    public static List<Component> getProgressTooltip(ItemStack stack) {
        List<Component> list = new ArrayList<>();

         if (!getMiningTooltip(stack).equals(Component.empty()))
            list.add(getMiningTooltip(stack));

        if (!getCraftTooltip(stack).equals(Component.empty()))
            list.add(getCraftTooltip(stack));

        if (!getFightTooltip(stack).equals(Component.empty()))
            list.add(getFightTooltip(stack));

        if (!getHealthTooltip(stack).equals(Component.empty()))
            list.add(getHealthTooltip(stack));

        return list;
    }

    private static Component getMiningTooltip(ItemStack stack) {
        int reqLevel = getToolReqLevel(ProgressType.MINING, stack);

        if (data.getGuiMiningLevelAmount() >= reqLevel)
            return Component.empty();

        if (reqLevel == -1)
            return Component.empty();
        else
            return Component.translatable("tooltip." + ThirteenFlames.MODID + ".progress.mining", reqLevel);
    }

    private static Component getCraftTooltip(ItemStack stack) {
        int reqLevel = getToolReqLevel(ProgressType.CRAFT, stack);

        if (data.getGuiCraftLevelAmount() >= reqLevel)
            return Component.empty();

        if (reqLevel == -1)
            return Component.empty();
        else
            return Component.translatable("tooltip." + ThirteenFlames.MODID + ".progress.craft", reqLevel);
    }

    private static Component getFightTooltip(ItemStack stack) {
        int reqLevel = getToolReqLevel(ProgressType.FIGHT, stack);

        if (data.getGuiFightLevelAmount() >= reqLevel)
            return Component.empty();

        if (reqLevel == -1)
            return Component.empty();
        else
            return Component.translatable("tooltip." + ThirteenFlames.MODID + ".progress.fight", reqLevel);
    }

    private static Component getHealthTooltip(ItemStack stack) {
        int reqLevel = getToolReqLevel(ProgressType.HEALTH, stack);

        if (data.getGuiHealthLevelAmount() >= reqLevel)
            return Component.empty();

        if (reqLevel == -1)
            return Component.empty();
        else
            return Component.translatable("tooltip." + ThirteenFlames.MODID + ".progress.health", reqLevel);
    }

    public static boolean isAllowUsage(ItemStack stack) {
        if (Minecraft.getInstance().player != null)
            if (Minecraft.getInstance().player.isCreative())
                return true;

        int maxCondition = 0;
        int allowCondition = 0;

        for (ProgressType type : ProgressType.values()) {
            int reqLevel = getToolReqLevel(type, stack);

            if (reqLevel == -1)
                continue;

            maxCondition++;

            if (type.equals(ProgressType.MINING))
                allowCondition += data.getGuiMiningLevelAmount() >= reqLevel ? 1 : 0;
            else if (type.equals(ProgressType.CRAFT))
                allowCondition += data.getGuiCraftLevelAmount() >= reqLevel ? 1 : 0;
            else if (type.equals(ProgressType.FIGHT))
                allowCondition += data.getGuiFightLevelAmount() >= reqLevel ? 1 : 0;
            else
                allowCondition += data.getGuiHealthLevelAmount() >= reqLevel ? 1 : 0;
        }

        if (maxCondition == allowCondition)
            return true;
        else
            return false;
    }

    public static boolean isAllowStatUsage(ProgressType type, ItemStack stack) {
        if (Minecraft.getInstance().player != null)
            if (Minecraft.getInstance().player.isCreative())
                return true;

        int reqLevel = getToolReqLevel(type, stack);

        if (type.equals(ProgressType.MINING))
            return data.getGuiMiningLevelAmount() >= reqLevel;
        else if (type.equals(ProgressType.CRAFT))
            return data.getGuiCraftLevelAmount() >= reqLevel;
        else if (type.equals(ProgressType.FIGHT))
            return data.getGuiFightLevelAmount() >= reqLevel;
        else
            return data.getGuiHealthLevelAmount() >= reqLevel;
    }

    private static int getToolReqLevel(ProgressType type, ItemStack stack) {
        for (ProgressHelper.ItemProgress itemProgress : ProgressHelper.ITEMS_REQ_PROGRESS) {
            if (Objects.equals(itemProgress.item(), stack.getItem())) {
                for (Pair<ProgressType, Integer> pair : itemProgress.itemReqP()) {
                    if (pair.getA().equals(type))
                        return Math.min(pair.getB(), 100);
                }
            }
        }

        return -1;
    }

    public enum ProgressType {
        MINING,
        CRAFT,
        FIGHT,
        HEALTH
    }
}
