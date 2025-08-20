package net.xXinailXx.thirteen_flames.mixin;

import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.phys.Vec3;
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
public class LivingEntityMixin {
    @Shadow private long lastDamageStamp;
    @Shadow protected ItemStack useItem;
    @Shadow protected int useItemRemaining;
    @Shadow private DamageSource lastDamageSource;
    @Unique private IData.IAbilitiesData data = new Data.AbilitiesData.Utils();

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;updateSwimAmount()V"), remap = false)
    public void tick(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity instanceof Player player)
            if (data.isActiveAbility(player, "desert_wind") && entity.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof BowItem && entity.isUsingItem())
                for (int i = 0; i < data.getLevelAbility(player, "desert_wind"); i++)
                    updatingUsingItem(entity);
    }

    @Unique
    private void updatingUsingItem(LivingEntity entity) {
        if (entity.isUsingItem()) {
            ItemStack itemStack = entity.getItemInHand(entity.getUsedItemHand());

            if (net.minecraftforge.common.ForgeHooks.canContinueUsing(this.useItem, itemStack))
                this.useItem = itemStack;

            if (itemStack == this.useItem) {
                if (!this.useItem.isEmpty()) {
                    useItemRemaining = net.minecraftforge.event.ForgeEventFactory.onItemUseTick(entity, useItem, useItemRemaining);

                    if (useItemRemaining > 0)
                        useItem.onUsingTick(entity, useItemRemaining);
                }

                updateUsingItem(entity, this.useItem);
            } else {
                entity.stopUsingItem();
            }
        }
    }

    @Unique
    private void updateUsingItem(LivingEntity entity, ItemStack stack) {
        stack.onUseTick(entity.level, entity, entity.getUseItemRemainingTicks());

        if (shouldTriggerItemUseEffects(entity))
            triggerItemUseEffects(entity, stack, 5);

        if (--this.useItemRemaining == 0 && !entity.level.isClientSide && !stack.useOnRelease())
            completeUsingItem(entity);
    }

    @Unique
    private boolean shouldTriggerItemUseEffects(LivingEntity entity) {
        int i = entity.getUseItemRemainingTicks();
        FoodProperties foodproperties = this.useItem.getFoodProperties(entity);
        boolean flag = foodproperties != null && foodproperties.isFastFood();
        flag |= i <= this.useItem.getUseDuration() - 7;

        return flag && i % 4 == 0;
    }

    @Unique
    private void triggerItemUseEffects(LivingEntity entity, ItemStack stack, int i) {
        if (!stack.isEmpty() && entity.isUsingItem()) {
            if (stack.getUseAnimation() == UseAnim.DRINK)
                entity.playSound(stack.getDrinkingSound(), 0.5F, entity.level.random.nextFloat() * 0.1F + 0.9F);

            if (stack.getUseAnimation() == UseAnim.EAT) {
                spawnItemParticles(entity, stack, i);
                entity.playSound(entity.getEatingSound(stack), 0.5F + 0.5F * (float)entity.getRandom().nextInt(2), (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2F + 1.0F);
            }
        }
    }

    @Unique
    private void completeUsingItem(LivingEntity entity) {
        if (!entity.level.isClientSide || entity.isUsingItem()) {
            InteractionHand interactionhand = entity.getUsedItemHand();

            if (!this.useItem.equals(entity.getItemInHand(interactionhand))) {
                entity.releaseUsingItem();
            } else {
                if (!this.useItem.isEmpty() && entity.isUsingItem()) {
                    triggerItemUseEffects(entity, this.useItem, 16);

                    ItemStack copy = this.useItem.copy();
                    ItemStack itemstack = net.minecraftforge.event.ForgeEventFactory.onItemUseFinish(entity, copy, entity.getUseItemRemainingTicks(), this.useItem.finishUsingItem(entity.level, entity));

                    if (itemstack != this.useItem)
                        entity.setItemInHand(interactionhand, itemstack);

                    entity.stopUsingItem();
                }
            }
        }
    }

    @Unique
    private void spawnItemParticles(LivingEntity entity, ItemStack stack, int i1) {
        for(int i = 0; i < i1; ++i) {
            Vec3 vec3 = new Vec3(((double)entity.getRandom().nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
            vec3 = vec3.xRot(-entity.getXRot() * ((float)Math.PI / 180F));
            vec3 = vec3.yRot(-entity.getYRot() * ((float)Math.PI / 180F));
            double d0 = (double)(-entity.getRandom().nextFloat()) * 0.6D - 0.3D;
            Vec3 vec31 = new Vec3(((double)entity.getRandom().nextFloat() - 0.5D) * 0.3D, d0, 0.6D);
            vec31 = vec31.xRot(-entity.getXRot() * ((float)Math.PI / 180F));
            vec31 = vec31.yRot(-entity.getYRot() * ((float)Math.PI / 180F));
            vec31 = vec31.add(entity.getX(), entity.getEyeY(), entity.getZ());

            if (entity.level instanceof ServerLevel)
                ((ServerLevel)entity.level).sendParticles(new ItemParticleOption(ParticleTypes.ITEM, stack), vec31.x, vec31.y, vec31.z, 1, vec3.x, vec3.y + 0.05D, vec3.z, 0.0D);
            else
                entity.level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, stack), vec31.x, vec31.y, vec31.z, vec3.x, vec3.y + 0.05D, vec3.z);
        }
    }

    @Inject(method = "getEquipmentSlotForItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getItem()Lnet/minecraft/world/item/Item;"), cancellable = true)
    private static void getEquipmentSlotForItem(ItemStack stack, CallbackInfoReturnable<EquipmentSlot> cir) {
        Item item = stack.getItem();

        if (!stack.is(Items.CARVED_PUMPKIN) && (!(item instanceof BlockItem) || !(((BlockItem)item).getBlock() instanceof AbstractSkullBlock))) {
            if (item instanceof ArmorItemTF) {
                cir.setReturnValue(((ArmorItemTF) item).getSlot());
                cir.cancel();
            }
        }
    }

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true, remap = false)
    public void hurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (Objects.equals(source, MoonBow.SUCC)) {
            LivingEntity entity = (LivingEntity) (Object) this;

            this.lastDamageSource = MoonBow.SUCC;
            this.lastDamageStamp = entity.level.getGameTime();
            float absorbedo = entity.getAbsorptionAmount();

            entity.setAbsorptionAmount(absorbedo - amount);

            amount -= absorbedo;

            if (amount > 0)
                entity.setHealth(entity.getHealth() - amount);

            cir.setReturnValue(true);
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
        LivingEntity entity = (LivingEntity) (Object) this;

        for(InteractionHand hand : InteractionHand.values()) {
            ItemStack stack = entity.getItemInHand(hand);

            if (!ProgressManager.isAllowUsage(stack)) {
                cir.setReturnValue(false);
                cir.cancel();
            }
        }
    }

    @Inject(method = "collectEquipmentChanges", at = @At(value = "RETURN"))
    private void collectEquipmentChanges(CallbackInfoReturnable<Map<EquipmentSlot, ItemStack>> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;

        for(EquipmentSlot slot : EquipmentSlot.values()) {
            if (!slot.getType().equals(EquipmentSlot.Type.ARMOR))
                continue;

            ItemStack stack = entity.getItemBySlot(slot);

            if (stack.is(Items.AIR))
                continue;

            if (ProgressManager.isAllowUsage(stack)) {
                entity.getAttributes().addTransientAttributeModifiers(stack.getAttributeModifiers(slot));
                return;
            }

            entity.getAttributes().removeAttributeModifiers(stack.getAttributeModifiers(slot));
        }
    }
}
