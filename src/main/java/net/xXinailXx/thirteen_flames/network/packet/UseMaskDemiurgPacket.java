package net.xXinailXx.thirteen_flames.network.packet;

import it.hurts.sskirillss.relics.items.relics.base.utils.AbilityUtils;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.xXinailXx.enderdragonlib.capability.PlayerCapManager;
import net.xXinailXx.thirteen_flames.init.ItemRegistry;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.PacketContext;

public class UseMaskDemiurgPacket implements IPacket {
    public void serverExecute(PacketContext ctx) {
        ServerPlayer player = ctx.getSender();

        if (player == null)
            return;

        ItemStack stack = player.getItemBySlot(EquipmentSlot.HEAD);

        if (!stack.is(ItemRegistry.MASK_DEMIURG.get()))
            return;

        if (player.getCooldowns().isOnCooldown(stack.getItem()))
            return;

        int time = (int) (AbilityUtils.getAbilityValue(stack, "dematerialization", "time") * 20);

        PlayerCapManager.addTimer(player, "tf_mask_demiurg_dematerialization", time);

        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, time, 2, true, true));

        player.getCooldowns().addCooldown(stack.getItem(), (int) (AbilityUtils.getAbilityValue(stack, "dematerialization", "cooldown") * 20));
    }
}
