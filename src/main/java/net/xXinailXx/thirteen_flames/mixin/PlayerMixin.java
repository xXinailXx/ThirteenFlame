package net.xXinailXx.thirteen_flames.mixin;

import it.hurts.sskirillss.relics.items.relics.base.utils.AbilityUtils;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;
import net.xXinailXx.thirteen_flames.data.IData;
import net.xXinailXx.thirteen_flames.data.Data;
import net.xXinailXx.thirteen_flames.init.ItemsRegistry;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Optional;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
    @Shadow public abstract ItemStack getItemBySlot(EquipmentSlot p_36257_);
    @Unique private IData.IAbilitiesData data = new Data.AbilitiesData.Utils();

    protected PlayerMixin(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    @Inject(method = "getCurrentItemAttackStrengthDelay", at = @At("RETURN"), cancellable = true)
    private void getCurrentItemAttackStrengthDelayWithFasterAttackSpeed(CallbackInfoReturnable<Float> ci) {
        Player player = (Player) (Object) this;

        Optional<ImmutableTriple<String, Integer, ItemStack>> optional = CuriosApi.getCuriosHelper().findEquippedCurio(ItemsRegistry.GLOVES_MONTU.get(), player);

        if (!optional.isEmpty()) {
            ItemStack curio = optional.get().getRight();

            if (!curio.isEmpty()) {
                double value = AbilityUtils.getAbilityValue(curio, "usin", "boost");

                ci.setReturnValue((float) (1.0D / this.getAttributeValue(Attributes.ATTACK_SPEED) * 20.0D * (1 - value * 0.01)));
            }
        }

        if (data.isActiveAbility("flowing_sand") && getItemBySlot(EquipmentSlot.MAINHAND).getItem() instanceof SwordItem)
            ci.setReturnValue(0F);
    }

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    public void hurt(DamageSource damageSource, float damage, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity) damageSource.getEntity();

        if (entity == null)
            return;

        if (data.isActiveAbility("retribution"))
            entity.hurt(DamageSource.MAGIC, (float)(damage * (data.getLevelAbility("retribution") * 0.02)));

        if (data.isActiveAbility("divine_veil")) {
            if (net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityUtils.isRandomSuccess(entity.getLevel(), data.getLevelAbility("divine_veil"))) {
                cir.setReturnValue(false);
                cir.cancel();
            }
        }
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;updateIsUnderwater()Z"))
    public void tick(CallbackInfo ci) {
        if (data.isActiveAbility("lord_elements")) {
            Player player = (Player) (Object) this;

            player.setAirSupply(player.getMaxAirSupply());
        }
    }
}
