package net.xXinailXx.thirteen_flames.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.DataSlot;
import net.xXinailXx.thirteen_flames.data.Data;
import net.xXinailXx.thirteen_flames.data.IData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AnvilMenu.class)
public class AnvilMenuMixin {
    @Shadow @Final private DataSlot cost;

    @Overwrite
    public int getCost() {
        IData.IAbilitiesData data = new Data.AbilitiesData.Utils();

        if (data.isActiveAbility(Minecraft.getInstance().player, "forge_of_legends"))
            return this.cost.get() > 1 ? 1 : 0;

        return this.cost.get();
    }
}
