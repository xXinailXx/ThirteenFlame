package net.xXinailXx.thirteen_flames.mixin;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.xXinailXx.thirteen_flames.item.base.ArmorItemTF;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public abstract class MobMixin {
    @Shadow public abstract boolean canReplaceEqualItem(ItemStack p_21478_, ItemStack p_21479_);

    @Inject(method = "canReplaceCurrentItem", at = @At(value = "HEAD"), cancellable = true)
    protected void canReplaceCurrentItem(ItemStack stack, ItemStack stack1, CallbackInfoReturnable<Boolean> cir) {
        if (stack1.isEmpty()) {
            cir.setReturnValue(true);
            cir.cancel();
        } else if (stack.getItem() instanceof ArmorItemTF) {
            if (EnchantmentHelper.hasBindingCurse(stack1)) {
                cir.setReturnValue(false);
                cir.cancel();
            } else if (!(stack1.getItem() instanceof ArmorItemTF)) {
                cir.setReturnValue(true);
                cir.cancel();
            } else {
                ArmorItemTF armoritem = (ArmorItemTF)stack.getItem();
                ArmorItemTF armoritem1 = (ArmorItemTF)stack1.getItem();

                if (armoritem.getDefense() != armoritem1.getDefense()) {
                    cir.setReturnValue(armoritem.getDefense() > armoritem1.getDefense());
                    cir.cancel();
                } else if (armoritem.getToughness() != armoritem1.getToughness()) {
                    cir.setReturnValue(armoritem.getToughness() > armoritem1.getToughness());
                    cir.cancel();
                } else {
                    cir.setReturnValue(this.canReplaceEqualItem(stack, stack1));
                    cir.cancel();
                }
            }
        }
    }
}
