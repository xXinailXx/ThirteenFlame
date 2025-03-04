package net.xXinailXx.thirteen_flames.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeHooks;
import net.xXinailXx.thirteen_flames.data.IData;
import net.xXinailXx.thirteen_flames.data.Data;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ForgeHooks.class)
public class ForgeHooksMixin {
    @Inject(method = "onLivingAttack", at = @At("HEAD"), remap = false)
    private static void onLivingAttack(LivingEntity entity, DamageSource src, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof Player player) {
            IData.IGuiLevelingData guiLevelingData = new Data.GuiLevelingData();
            IData.IAbilitiesData abilitiesData = new Data.AbilitiesData.Utils();

            if (abilitiesData.isActiveAbility("resistance"))
                amount = amount / 2;

            amount = (float) (amount - (amount * (Math.round((float) guiLevelingData.getGuiFightLevelAmount() / 10) * 0.01)));
        }
    }
}
