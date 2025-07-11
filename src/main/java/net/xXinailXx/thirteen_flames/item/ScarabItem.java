package net.xXinailXx.thirteen_flames.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.xXinailXx.thirteen_flames.data.Data;
import net.xXinailXx.thirteen_flames.data.IData;
import net.xXinailXx.thirteen_flames.item.base.ItemSetting;
import net.xXinailXx.thirteen_flames.utils.ScarabsType;

public class ScarabItem extends ItemSetting {
    private final ScarabsType type;

    public ScarabItem(ScarabsType type) {
        this.type = type;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        IData.IScarabsData data = new Data.ScarabsData.Utils();

        if (level.isClientSide) {
            if (this.type == ScarabsType.SILVER)
                data.addScarabSilver(player, stack.getCount());
        } else {
            switch (this.type) {
                case GOLD -> data.addScarabGold(player, stack.getCount());
                case AURITEH -> data.addScarabAuriteh(player, stack.getCount());
                case LAZOTEP -> data.addScarabLazotep(player, stack.getCount());
            }
        }

        if (!player.isCreative())
            stack.shrink(stack.getCount());

        return super.use(level, player, hand);
    }
}
