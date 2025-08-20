package net.xXinailXx.thirteen_flames.item;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.xXinailXx.enderdragonlib.client.message.MessageManager;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.data.Data;
import net.xXinailXx.thirteen_flames.data.IData;
import net.xXinailXx.thirteen_flames.item.base.ItemSetting;
import net.xXinailXx.thirteen_flames.utils.ScarabsType;

import java.util.Locale;

public class ScarabItem extends ItemSetting {
    private final ScarabsType type;

    public ScarabItem(ScarabsType type) {
        this.type = type;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        IData.IScarabsData data = new Data.ScarabsData.Utils();

        String path = switch (this.type) {
            case SILVER -> "textures/gui/icon/scarab_silver_icon.png";
            case GOLD -> "textures/gui/icon/scarab_gold_icon.png";
            case AURITEH -> "textures/gui/icon/scarab_auriteh_icon.png";
            case LAZOTEP -> "textures/gui/icon/scarab_lazotep_icon.png";
        };

        if (!player.isShiftKeyDown()) {
            if (level.isClientSide) {
                if (this.type == ScarabsType.SILVER)
                    data.addScarabSilver(player, 1);
            } else {
                switch (this.type) {
                    case GOLD -> data.addScarabGold(player, 1);
                    case AURITEH -> data.addScarabAuriteh(player, 1);
                    case LAZOTEP -> data.addScarabLazotep(player, 1);
                }
            }

            if (!player.isCreative())
                stack.split(1);

            MessageManager.addMessage(new ResourceLocation(ThirteenFlames.MODID, path), Component.translatable("message.thirteen_flames.add_scarab", 1));
        } else {
            int count = player.isCreative() ? 64 : stack.getCount();

            if (level.isClientSide) {
                if (this.type == ScarabsType.SILVER)
                    data.addScarabSilver(player, count);
            } else {
                switch (this.type) {
                    case GOLD -> data.addScarabGold(player, count);
                    case AURITEH -> data.addScarabAuriteh(player, count);
                    case LAZOTEP -> data.addScarabLazotep(player, count);
                }
            }

            if (!player.isCreative())
                stack.split(count);

            MessageManager.addMessage(new ResourceLocation(ThirteenFlames.MODID, path), Component.translatable("message.thirteen_flames.add_scarab", count));
        }

        return super.use(level, player, hand);
    }
}
