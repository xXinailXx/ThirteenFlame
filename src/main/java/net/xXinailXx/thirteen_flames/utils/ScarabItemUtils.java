package net.xXinailXx.thirteen_flames.utils;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.xXinailXx.thirteen_flames.data.Data;
import net.xXinailXx.thirteen_flames.data.IData;

public class ScarabItemUtils extends ItemSetting {
    private final ScarabsType type;

    public ScarabItemUtils(ScarabsType type) {
        this.type = type;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        IData.IScarabsData data = new Data.ScarabsData();

        if (level.isClientSide) {
            if (this.type == ScarabsType.SILVER)
                data.addScarabSilver(player, stack.getCount());
        } else {
            switch (this.type) {
                case GOLD -> data.addScarabGold(stack.getCount());
                case AURITEH -> data.addScarabAuriteh(stack.getCount());
                case LAZOTEP -> data.addScarabLazotep(stack.getCount());
            }
        }

        stack.shrink(stack.getCount());
        return super.use(level, player, hand);
    }
}
