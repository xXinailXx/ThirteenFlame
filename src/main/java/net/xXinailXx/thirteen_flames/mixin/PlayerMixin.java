package net.xXinailXx.thirteen_flames.mixin;

import it.hurts.sskirillss.relics.items.relics.base.utils.AbilityUtils;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;
import net.xXinailXx.enderdragonlib.utils.MathUtils;
import net.xXinailXx.thirteen_flames.data.IData;
import net.xXinailXx.thirteen_flames.data.Data;
import net.xXinailXx.thirteen_flames.init.ItemRegistry;
import net.xXinailXx.thirteen_flames.item.flame.MoonBow;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Objects;
import java.util.Optional;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
    @Shadow public abstract ItemStack getItemBySlot(EquipmentSlot p_36257_);
    @Shadow public abstract Iterable<ItemStack> getArmorSlots();
    @Unique private IData.IAbilitiesData data = new Data.AbilitiesData.Utils();

    protected PlayerMixin(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    @Inject(method = "getCurrentItemAttackStrengthDelay", at = @At("RETURN"), cancellable = true)
    private void getCurrentItemAttackStrengthDelay(CallbackInfoReturnable<Float> ci) {
        Player player = (Player) (Object) this;

        Optional<ImmutableTriple<String, Integer, ItemStack>> optional = CuriosApi.getCuriosHelper().findEquippedCurio(ItemRegistry.GLOVES_MONTU.get(), player);

        if (optional.isPresent()) {
            ItemStack curio = optional.get().getRight();

            if (!curio.isEmpty()) {
                int value = (int) AbilityUtils.getAbilityValue(curio, "usin", "boost");

                System.out.println(value);
                System.out.println(ci.getReturnValueF());

                ci.setReturnValue(ci.getReturnValueF() * (1 - value * 0.01F));

                System.out.println(ci.getReturnValueF());

                ci.cancel();
            }
        }

        if (data.isActiveAbility(player, "flowing_sand") && getItemBySlot(EquipmentSlot.MAINHAND).getItem() instanceof SwordItem)
            ci.setReturnValue(0F);
    }

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    public void hurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (Objects.equals(source, MoonBow.SUCC)) {
            super.hurt(source, amount);

            cir.cancel();
        }

        LivingEntity entity = (LivingEntity) source.getEntity();

        if (entity == null)
            return;

        Player player = (Player) (Object) this;

        if (data.isActiveAbility(player, "retribution"))
            entity.hurt(DamageSource.MAGIC, (float)(amount * (data.getLevelAbility(player, "retribution") * 0.02)));

        if (data.isActiveAbility(player, "divine_veil")) {
            if (MathUtils.isRandom(entity.getLevel(), data.getLevelAbility(player, "divine_veil"))) {
                cir.setReturnValue(false);
                cir.cancel();
            }
        }
    }
}
