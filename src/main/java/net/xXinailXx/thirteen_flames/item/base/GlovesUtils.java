package net.xXinailXx.thirteen_flames.item.base;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class GlovesUtils extends LivingEntity {
    protected GlovesUtils(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super( p_20966_, p_20967_ );
    }

    public void updatingUsing() {
        if (this.isUsingItem()) {
            ItemStack itemStack = this.getItemInHand(this.getUsedItemHand());
            if (net.minecraftforge.common.ForgeHooks.canContinueUsing(this.useItem, itemStack)) this.useItem = itemStack;
            if (itemStack == this.useItem) {

                if (!this.useItem.isEmpty()) {
                    this.useItemRemaining = net.minecraftforge.event.ForgeEventFactory.onItemUseTick(this, this.useItem, this.useItemRemaining);
                    if (useItemRemaining > 0)
                        this.useItem.onUsingTick(this, this.useItemRemaining);
                }

                this.updateUsingItem(this.useItem);
            } else {
                this.stopUsingItem();
            }
        }
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return null;
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot p_21127_) {
        return null;
    }

    @Override
    public void setItemSlot(EquipmentSlot p_21036_, ItemStack p_21037_) {

    }

    @Override
    public HumanoidArm getMainArm() {
        return null;
    }
}
