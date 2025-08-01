package net.xXinailXx.thirteen_flames.mixin;

import com.mojang.authlib.GameProfile;
import it.hurts.sskirillss.relics.items.relics.base.utils.AbilityUtils;
import it.hurts.sskirillss.relics.items.relics.base.utils.ResearchUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.ProfilePublicKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;
import net.xXinailXx.enderdragonlib.utils.MathUtils;
import net.xXinailXx.thirteen_flames.data.IData;
import net.xXinailXx.thirteen_flames.data.Data;
import net.xXinailXx.thirteen_flames.init.ItemRegistry;
import net.xXinailXx.thirteen_flames.item.flame.MoonBow;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.jetbrains.annotations.Nullable;
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
public abstract class PlayerMixin extends Player {
    @Shadow public abstract ItemStack getItemBySlot(EquipmentSlot p_36257_);
    @Shadow public abstract Iterable<ItemStack> getArmorSlots();
    @Unique private IData.IAbilitiesData data = new Data.AbilitiesData.Utils();

    public PlayerMixin(Level p_219727_, BlockPos p_219728_, float p_219729_, GameProfile p_219730_, @Nullable ProfilePublicKey p_219731_) {
        super(p_219727_, p_219728_, p_219729_, p_219730_, p_219731_);
    }

    @Inject(method = "getCurrentItemAttackStrengthDelay", at = @At("RETURN"), cancellable = true)
    private void getCurrentItemAttackStrengthDelayWithFasterAttackSpeed(CallbackInfoReturnable<Float> ci) {
        Player player = (Player) (Object) this;

        Optional<ImmutableTriple<String, Integer, ItemStack>> optional = CuriosApi.getCuriosHelper().findEquippedCurio(ItemRegistry.GLOVES_MONTU.get(), player);

        if (!optional.isEmpty()) {
            ItemStack curio = optional.get().getRight();

            if (!curio.isEmpty()) {
                double value = AbilityUtils.getAbilityValue(curio, "usin", "boost");

                ci.setReturnValue((float) (1.0D / this.getAttributeValue(Attributes.ATTACK_SPEED) * 20.0D * (1 - value * 0.01)));
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
