package net.xXinailXx.thirteen_flames.mixin;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.xXinailXx.thirteen_flames.client.progress.ProgressManager;
import net.xXinailXx.thirteen_flames.data.IData;
import net.xXinailXx.thirteen_flames.data.Data;
import net.xXinailXx.thirteen_flames.item.base.ArmorItemTF;
import net.xXinailXx.thirteen_flames.item.flame.MoonBow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Objects;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow public abstract ItemStack getItemInHand(InteractionHand p_21121_);
    @Shadow public abstract boolean isUsingItem();
    @Shadow protected abstract void updatingUsingItem();
    @Shadow @Nullable private DamageSource lastDamageSource;
    @Shadow private long lastDamageStamp;
    @Shadow public abstract double getAttributeValue(Attribute p_21134_);
    @Shadow public abstract ItemStack getItemBySlot(EquipmentSlot p_21127_);
    @Shadow public abstract AttributeMap getAttributes();
    @Unique private IData.IAbilitiesData data = new Data.AbilitiesData.Utils();

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;updatingUsingItem()V"))
    public void tick(CallbackInfo ci) {
        if (data.isActiveAbility("desert_wind") && this.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof BowItem && this.isUsingItem()) {
            for (int i = 0; i < data.getLevelAbility("desert_wind"); i++)
                this.updatingUsingItem();
        }
    }

    @Inject(method = "getEquipmentSlotForItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;canPerformAction(Lnet/minecraftforge/common/ToolAction;)Z"), remap = false, cancellable = true)
    private static void getEquipmentSlotForItem(ItemStack stack, CallbackInfoReturnable<EquipmentSlot> cir) {
        if (stack.getItem() instanceof ArmorItemTF) {
            cir.setReturnValue(((ArmorItemTF) stack.getItem()).getSlot());
            cir.cancel();
        }
    }

    @Inject(method = "hurt", at = @At("HEAD"))
    public void hurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (Objects.equals(source, MoonBow.SUCC)) {
            this.lastDamageSource = MoonBow.SUCC;
            this.lastDamageStamp = ((LivingEntity) (Object) this).level.getGameTime();
            float absorbedo = ((LivingEntity) (Object) this).getAbsorptionAmount();
            ((LivingEntity) (Object) this).setAbsorptionAmount(absorbedo - amount);
            amount -= absorbedo;

            if (amount > 0)
                ((LivingEntity) (Object) this).setHealth(((LivingEntity) (Object) this).getHealth() - amount);

            cir.cancel();
        }
    }

    @Inject(method = "getHurtSound", at = @At("HEAD"), cancellable = true)
    public void getHurtSound(DamageSource source, CallbackInfoReturnable<SoundSource> cir) {
        if (Objects.equals(source, MoonBow.SUCC)) {
            cir.setReturnValue(null);
            cir.cancel();
        }
    }

    @Inject(method = "checkTotemDeathProtection", at = @At("HEAD"), cancellable = true)
    private void checkTotemDeathProtection(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        for(InteractionHand hand : InteractionHand.values()) {
            ItemStack stack = this.getItemInHand(hand);

            if (!ProgressManager.isAllowUsage(stack)) {
                cir.setReturnValue(false);
                cir.cancel();
            }
        }
    }

    @Inject(method = "collectEquipmentChanges", at = @At(value = "RETURN"))
    private void collectEquipmentChanges(CallbackInfoReturnable<Map<EquipmentSlot, ItemStack>> cir) {
        for(EquipmentSlot slot : EquipmentSlot.values()) {
            if (!slot.getType().equals(EquipmentSlot.Type.ARMOR))
                continue;

            ItemStack stack = this.getItemBySlot(slot);

            if (stack.is(Items.AIR))
                continue;

            if (ProgressManager.isAllowUsage(stack)) {
                this.getAttributes().addTransientAttributeModifiers(stack.getAttributeModifiers(slot));
                return;
            }

            this.getAttributes().removeAttributeModifiers(stack.getAttributeModifiers(slot));
        }
    }
}
