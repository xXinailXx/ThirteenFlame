package net.xXinailXx.thirteen_flames.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.xXinailXx.thirteen_flames.data.IData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityUtils;
import net.xXinailXx.thirteen_flames.data.Data;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Unique private IData.IAbilitiesData data = new Data.AbilitiesData.Utils();

    @Inject(method = "hurt", at = @At(value = "HEAD"))
    public void hurt(int p_220158_, RandomSource source, ServerPlayer player, CallbackInfoReturnable<Boolean> cir) {
        if (data.isActiveAbility(player, "careful_handling")) {
            if (AbilityUtils.isRandomSuccess(player.getLevel(), data.getLevelAbility(player, "careful_handling")))
                cir.cancel();
        }
    }
}
