package net.xXinailXx.thirteen_flames.mixin;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.xXinailXx.thirteen_flames.data.IData;
import net.xXinailXx.thirteen_flames.data.Data;
import net.xXinailXx.thirteen_flames.item.base.ArmorItemTF;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow public abstract ItemStack getItemInHand(InteractionHand p_21121_);
    @Shadow public abstract boolean isUsingItem();
    @Shadow protected abstract void updatingUsingItem();
    @Unique private IData.IAbilitiesData data = new Data.AbilitiesData.Utils();

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;updatingUsingItem()V"))
    public void tick(CallbackInfo ci) {
        if (data.isActiveAbility("desert_wind") && this.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof BowItem && this.isUsingItem()) {
            for (int i = 0; i < data.getLevelAbility("desert_wind"); i++) {
                this.updatingUsingItem();
            }
        }
    }

    @Inject(method = "getEquipmentSlotForItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;canPerformAction(Lnet/minecraftforge/common/ToolAction;)Z"), remap = false, cancellable = true)
    private static void getEquipmentSlotForItem(ItemStack stack, CallbackInfoReturnable<EquipmentSlot> cir) {
        if (stack.getItem() instanceof ArmorItemTF) {
            cir.setReturnValue(((ArmorItemTF) stack.getItem()).getSlot());
            cir.cancel();
        }
    }
}
