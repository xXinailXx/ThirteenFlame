package net.xXinailXx.thirteen_flames.mixin;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.xXinailXx.thirteen_flames.item.base.ArmorItemTF;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Mob.class)
public abstract class MobMixin {
    @Shadow public abstract boolean canReplaceEqualItem(ItemStack p_21478_, ItemStack p_21479_);

    @Overwrite(remap = false)
    protected boolean canReplaceCurrentItem(ItemStack stack, ItemStack stack1) {
        if (stack1.isEmpty()) {
            return true;
        } else if (stack.getItem() instanceof SwordItem) {
            if (!(stack1.getItem() instanceof SwordItem)) {
                return true;
            } else {
                SwordItem sworditem = (SwordItem)stack.getItem();
                SwordItem sworditem1 = (SwordItem)stack1.getItem();
                if (sworditem.getDamage() != sworditem1.getDamage()) {
                    return sworditem.getDamage() > sworditem1.getDamage();
                } else {
                    return this.canReplaceEqualItem(stack, stack1);
                }
            }
        } else if (stack.getItem() instanceof BowItem && stack1.getItem() instanceof BowItem) {
            return this.canReplaceEqualItem(stack, stack1);
        } else if (stack.getItem() instanceof CrossbowItem && stack1.getItem() instanceof CrossbowItem) {
            return this.canReplaceEqualItem(stack, stack1);
        } else if (stack.getItem() instanceof ArmorItemTF) {
            if (EnchantmentHelper.hasBindingCurse(stack1)) {
                return false;
            } else if (!(stack1.getItem() instanceof ArmorItem)) {
                return true;
            } else {
                ArmorItemTF armoritem = (ArmorItemTF)stack.getItem();
                ArmorItemTF armoritem1 = (ArmorItemTF)stack1.getItem();
                if (armoritem.getDefense() != armoritem1.getDefense()) {
                    return armoritem.getDefense() > armoritem1.getDefense();
                } else if (armoritem.getToughness() != armoritem1.getToughness()) {
                    return armoritem.getToughness() > armoritem1.getToughness();
                } else {
                    return this.canReplaceEqualItem(stack, stack1);
                }
            }
        } else if (stack.getItem() instanceof ArmorItem) {
            if (EnchantmentHelper.hasBindingCurse(stack1)) {
                return false;
            } else if (!(stack1.getItem() instanceof ArmorItem)) {
                return true;
            } else {
                ArmorItem armoritem = (ArmorItem)stack.getItem();
                ArmorItem armoritem1 = (ArmorItem)stack1.getItem();
                if (armoritem.getDefense() != armoritem1.getDefense()) {
                    return armoritem.getDefense() > armoritem1.getDefense();
                } else if (armoritem.getToughness() != armoritem1.getToughness()) {
                    return armoritem.getToughness() > armoritem1.getToughness();
                } else {
                    return this.canReplaceEqualItem(stack, stack1);
                }
            }
        } else {
            if (stack.getItem() instanceof DiggerItem) {
                if (stack1.getItem() instanceof BlockItem) {
                    return true;
                }

                if (stack1.getItem() instanceof DiggerItem) {
                    DiggerItem diggeritem = (DiggerItem)stack.getItem();
                    DiggerItem diggeritem1 = (DiggerItem)stack1.getItem();
                    if (diggeritem.getAttackDamage() != diggeritem1.getAttackDamage()) {
                        return diggeritem.getAttackDamage() > diggeritem1.getAttackDamage();
                    }

                    return this.canReplaceEqualItem(stack, stack1);
                }
            }

            return false;
        }
    }
}
