package net.xXinailXx.thirteen_flames.mixin;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.xXinailXx.thirteen_flames.client.progress.ProgressManager;
import net.xXinailXx.thirteen_flames.data.IData;
import net.xXinailXx.thirteen_flames.data.Data;
import net.xXinailXx.thirteen_flames.item.base.ArmorItemTF;
import net.xXinailXx.thirteen_flames.item.flame.MoonBow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
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
    @Shadow public abstract boolean isUsingItem();
    @Shadow protected abstract void updatingUsingItem();
    @Shadow @Nullable private DamageSource lastDamageSource;
    @Shadow private long lastDamageStamp;
    @Shadow public abstract ItemStack getItemBySlot(EquipmentSlot p_21127_);
    @Shadow public abstract AttributeMap getAttributes();
    @Shadow public abstract ItemStack getItemInHand(InteractionHand p_21121_);
    @Unique private IData.IAbilitiesData data = new Data.AbilitiesData.Utils();

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;updatingUsingItem()V"))
    public void tick(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity instanceof Player player)
            if (data.isActiveAbility(player, "desert_wind") && this.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof BowItem && this.isUsingItem())
                for (int i = 0; i < data.getLevelAbility(player, "desert_wind"); i++)
                    this.updatingUsingItem();
    }

    @Overwrite
    public static EquipmentSlot getEquipmentSlotForItem(ItemStack stack) {
        final EquipmentSlot slot = stack.getEquipmentSlot();

        if (slot != null)
            return slot;

        Item item = stack.getItem();

        if (!stack.is(Items.CARVED_PUMPKIN) && (!(item instanceof BlockItem) || !(((BlockItem)item).getBlock() instanceof AbstractSkullBlock)))
            if (item instanceof ArmorItem)
                return ((ArmorItem)item).getSlot();
            else if (item instanceof ArmorItemTF)
                return ((ArmorItemTF)item).getSlot();
            else if (stack.is(Items.ELYTRA))
                return EquipmentSlot.CHEST;
            else
                return stack.canPerformAction(net.minecraftforge.common.ToolActions.SHIELD_BLOCK) ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND;
        else
            return EquipmentSlot.HEAD;
    }

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true, remap = false)
    public void hurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (Objects.equals(source, MoonBow.SUCC)) {
            this.lastDamageSource = MoonBow.SUCC;
            this.lastDamageStamp = ((LivingEntity) (Object) this).level.getGameTime();
            float absorbedo = ((LivingEntity) (Object) this).getAbsorptionAmount();
            ((LivingEntity) (Object) this).setAbsorptionAmount(absorbedo - amount);
            amount -= absorbedo;

            if (amount > 0)
                ((LivingEntity) (Object) this).setHealth(((LivingEntity) (Object) this).getHealth() - amount);

            cir.setReturnValue(true);
            cir.cancel();
        }
    }

    @Inject(method = "getHurtSound", at = @At("HEAD"), cancellable = true, remap = false)
    public void getHurtSound(DamageSource source, CallbackInfoReturnable<SoundSource> cir) {
        if (Objects.equals(source, MoonBow.SUCC)) {
            cir.setReturnValue(null);
            cir.cancel();
        }
    }

    @Inject(method = "checkTotemDeathProtection", at = @At("HEAD"), cancellable = true, remap = false)
    private void checkTotemDeathProtection(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        for(InteractionHand hand : InteractionHand.values()) {
            ItemStack stack = this.getItemInHand(hand);

            if (!ProgressManager.isAllowUsage(stack)) {
                cir.setReturnValue(false);
                cir.cancel();
            }
        }
    }

    @Inject(method = "collectEquipmentChanges", at = @At(value = "RETURN"), remap = false)
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
