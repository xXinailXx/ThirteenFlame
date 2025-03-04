package net.xXinailXx.thirteen_flames.mixin;

import it.hurts.sskirillss.relics.init.EffectRegistry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.phys.EntityHitResult;
import net.xXinailXx.thirteen_flames.data.IData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityUtils;
import net.xXinailXx.thirteen_flames.data.Data;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractArrow.class)
public class AbstractArrowMixin {
    @Unique private IData.IAbilitiesData data = new Data.AbilitiesData.Utils();
    @Unique private Entity owner;

    @Inject(method = "onHitEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;doPostHurtEffects(Lnet/minecraft/world/entity/LivingEntity;)V"))
    protected void onHitEntity(EntityHitResult result, CallbackInfo ci) {
        if (data.isActiveAbility("cobra_poison")) {
            if (!data.isActiveAbility("arrow_anubis") && !data.isActiveAbility("fang_frost")) {
                AbilityUtils.getSurroundingEntities((LivingEntity) result.getEntity(), 2).forEach(entity -> {
                    if (this.owner != null && !entity.equals(this.owner)) {
                        if (AbilityUtils.isRandomSuccess(result.getEntity().getLevel(), data.getLevelAbility("cobra_poison") * 10))
                            entity.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 5));
                    }
                });
            }
        }

        if (data.isActiveAbility("arrow_anubis")) {
            if (!data.isActiveAbility("cobra_poison") && !data.isActiveAbility("fang_frost")) {
                AbilityUtils.getSurroundingEntities((LivingEntity) result.getEntity(), 2).forEach(entity -> {
                    if (this.owner != null && ! entity.equals(this.owner)) {
                        if (AbilityUtils.isRandomSuccess(result.getEntity().getLevel(), data.getLevelAbility("arrow_anubis") * 10))
                            entity.addEffect(new MobEffectInstance(MobEffects.WITHER, 100, 5));
                        if (AbilityUtils.isRandomSuccess(result.getEntity().getLevel(), data.getLevelAbility("arrow_anubis")))
                            entity.hurt(DamageSource.MAGIC, entity.getHealth());
                    }
                });
            }
        }

        if (data.isActiveAbility("fang_frost")) {
            if (!data.isActiveAbility("cobra_poison") && !data.isActiveAbility("arrow_anubis")) {
                if (AbilityUtils.isRandomSuccess(result.getEntity().getLevel(), data.getLevelAbility("fang_frost") * 10)) {
                    AbilityUtils.getSurroundingEntities((LivingEntity) result.getEntity(), 2).forEach(entity -> {
                        if (this.owner != null && ! entity.equals(this.owner))
                            entity.addEffect(new MobEffectInstance(EffectRegistry.STUN.get(), 100));
                    });
                }
            }
        }
    }

    @Inject(method = "setOwner", at = @At(value = "TAIL"))
    public void setOwner(Entity entity, CallbackInfo ci) {
        this.owner = entity;
    }
}
