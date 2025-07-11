package net.xXinailXx.thirteen_flames.mixin;

import it.hurts.sskirillss.relics.init.EffectRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.xXinailXx.thirteen_flames.data.IData;
import net.xXinailXx.thirteen_flames.data.Data;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(FishingHook.class)
public abstract class FishingHookMixin {
    @Shadow @Nullable public abstract Entity getHookedIn();
    @Shadow @Nullable public abstract Player getPlayerOwner();
    @Unique private IData.IAbilitiesData data = new Data.AbilitiesData.Utils();

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/FishingHook;setPos(DDD)V"))
    public void hookedEntity(CallbackInfo ci) {
        if (data.isActiveAbility(Minecraft.getInstance().player, "fisherman_of_fisherman")) {
            LivingEntity living = (LivingEntity) this.getHookedIn();

            if (!living.hasEffect(EffectRegistry.STUN.get())) {
                if (this.getHookedIn() instanceof LivingEntity) {
                    this.getHookedIn().hurt(DamageSource.playerAttack(this.getPlayerOwner()), 1);
                    living.addEffect(new MobEffectInstance(EffectRegistry.STUN.get(), 100), living);
                }
            }
        }
    }
}
