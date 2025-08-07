package net.xXinailXx.thirteen_flames.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
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
            IData.IGuiLevelingData guiLevelingData = new Data.GuiLevelingData.Utils();
            IData.IAbilitiesData abilitiesData = new Data.AbilitiesData.Utils();

            if (abilitiesData.isActiveAbility(player, "resistance"))
                amount = amount / 2;
            else
                return;

            amount = (float) (amount - (amount * (Math.round((float) guiLevelingData.getFightLevel(player) / 10) * 0.01)));

            cir.setReturnValue(entity instanceof Player || !MinecraftForge.EVENT_BUS.post(new LivingAttackEvent(entity, src, amount)));
            cir.cancel();
        }
    }
}
