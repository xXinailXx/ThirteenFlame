package net.xXinailXx.thirteen_flames.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.world.inventory.DataSlot;
import net.xXinailXx.thirteen_flames.data.IData;
import net.xXinailXx.thirteen_flames.data.Data;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

@Mixin(DataSlot.class)
public class DataSlotMixin {
    @Unique private static IData.IAbilitiesData data = new Data.AbilitiesData.Utils();

    @Overwrite
    public static DataSlot standalone() {
        return new DataSlot() {
            private int value;

            public int get() {
                if (data.isActiveAbility(Minecraft.getInstance().player, "forge_of_legends"))
                    return 1;
                else
                    return value;
            }

            public void set(int value) {
                if (!data.isActiveAbility(Minecraft.getInstance().player, "forge_of_legends"))
                    this.value = value;
            }
        };
    }
}
