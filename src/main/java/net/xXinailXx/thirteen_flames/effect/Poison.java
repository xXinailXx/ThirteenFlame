package net.xXinailXx.thirteen_flames.effect;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

import java.awt.*;

public class Poison extends MobEffect {
    public Poison(MobEffectCategory category, int color) {
        super(category, color);
    }

    public void applyEffectTick(LivingEntity entity, int amplifire) {
        super.applyEffectTick(entity, amplifire);

        int invulTime = entity.invulnerableTime;

        entity.hurt(DamageSource.MAGIC, 1F + amplifire * 0.2F);

        entity.invulnerableTime = invulTime;
    }

    public boolean isDurationEffectTick(int duration, int amplifier) {
        int j = 20 - amplifier * 3;

        if (j > 0)
            return duration % j == 0;
        else
            return true;
    }
}
