package net.xXinailXx.thirteen_flames.effect;

import lombok.Getter;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.xXinailXx.thirteen_flames.utils.Gods;

@Getter
public class StatueEffect extends MobEffect {
    private final Gods god;

    protected StatueEffect(int color, Gods god) {
        super(MobEffectCategory.BENEFICIAL, color);
        this.god = god;
    }
}
