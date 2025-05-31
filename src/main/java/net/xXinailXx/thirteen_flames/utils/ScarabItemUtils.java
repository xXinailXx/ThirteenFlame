package net.xXinailXx.thirteen_flames.utils;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.data.Data;
import net.xXinailXx.thirteen_flames.data.IData;

public class ScarabItemUtils extends ItemSetting {
    private final ScarabsType type;

    public ScarabItemUtils(ScarabsType type) {
        this.type = type;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide)
            return super.use(level, player, hand);

        ItemStack stack = player.getItemInHand(hand);

        IData.IScarabsData data = new Data.ScarabsData();

        switch (this.type) {
            case SILVER -> data.addScarabSilver(player, stack.getCount());
            case GOLD -> data.addScarabGold(stack.getCount());
            case AURITEH -> data.addScarabAuriteh(stack.getCount());
            case LAZOTEP -> data.addScarabLazotep(stack.getCount());
        }

        addMessageScarabs();

        stack.shrink(stack.getCount());

        return super.use(level, player, hand);
    }

    public void addMessageScarabs() {
        String scarabName = "";

        switch (this.type) {
            case SILVER -> scarabName = "silver";
            case GOLD -> scarabName = "gold";
            case AURITEH -> scarabName = "auriteh";
            case LAZOTEP -> scarabName = "lazotep";
        }

        String scarabComponent = Component.translatable("message." + ThirteenFlames.MODID + ".scarab_type." + scarabName).getString();
    }
}
