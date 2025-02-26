package net.xXinailXx.thirteen_flames.mixin;

import net.minecraft.world.food.FoodProperties;
import net.xXinailXx.thirteen_flames.data.Data;
import net.xXinailXx.thirteen_flames.data.IData;
import org.spongepowered.asm.mixin.*;

@Mixin(FoodProperties.class)
public class FoodPropertiesMixin {
    @Shadow @Final private float saturationModifier;
    @Unique
    private IData.IAbilitiesData data = new Data.AbilitiesData.Utils();

    @Overwrite
    public float getSaturationModifier() {
        return (float) (this.saturationModifier * ((data.isActiveAbility("diet") ? (data.getLevelAbility("diet") + 1) * 0.05 : 0) + 1));
    }
}
