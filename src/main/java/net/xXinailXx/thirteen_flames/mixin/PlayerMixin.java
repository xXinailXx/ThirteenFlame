package net.xXinailXx.thirteen_flames.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.xXinailXx.thirteen_flames.data.IData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityUtils;
import net.xXinailXx.thirteen_flames.data.Data;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerMixin {
    @Shadow public abstract ItemStack getItemBySlot(EquipmentSlot p_36257_);
    @Unique private IData.IAbilitiesData data = new Data.AbilitiesData.Utils();

    @Inject(method = "getCurrentItemAttackStrengthDelay", at = @At("RETURN"), cancellable = true)
    private void getCurrentItemAttackStrengthDelayWithFasterAttackSpeed(CallbackInfoReturnable<Float> ci) {
        if (data.isActiveAbility("flowing_sand") && getItemBySlot(EquipmentSlot.MAINHAND).getItem() instanceof SwordItem) {
            ci.setReturnValue(0F);
        }
    }

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    public void hurt(DamageSource damageSource, float damage, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity) damageSource.getEntity();

        if (entity == null) {
            return;
        }

        if (data.isActiveAbility("retribution")) {
            entity.hurt(DamageSource.MAGIC, (float)(damage * (data.getLevelAbility("retribution") * 0.02)));
        }
        if (data.isActiveAbility("divine_veil")) {
            if (AbilityUtils.isRandomSuccess(entity.getLevel(), data.getLevelAbility("divine_veil"))) {
                cir.setReturnValue(false);
                cir.cancel();
            }
        }
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;updateIsUnderwater()Z"))
    public void tick(CallbackInfo ci) {
        if (data.isActiveAbility("lord_elements")) {
            Player player = (Player) (Object) this;

            player.setAirSupply(player.getMaxAirSupply());
        }
    }
}
