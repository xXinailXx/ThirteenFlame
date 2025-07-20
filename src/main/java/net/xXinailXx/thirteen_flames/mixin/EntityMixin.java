package net.xXinailXx.thirteen_flames.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.xXinailXx.thirteen_flames.data.Data;
import net.xXinailXx.thirteen_flames.data.IData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {
    @Shadow @Final protected SynchedEntityData entityData;
    @Unique private IData.IAbilitiesData data = new Data.AbilitiesData.Utils();

    @Inject(method = "setSecondsOnFire", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/ProtectionEnchantment;getFireAfterDampener(Lnet/minecraft/world/entity/LivingEntity;I)I"), remap = false, cancellable = true)
    public void setSecondsOnFire(int i, CallbackInfo ci) {
        if (((Entity) (Object) this) instanceof Player player && data.isActiveAbility(player, "lord_elements"))
            ci.cancel();
    }

    @Inject(method = "setRemainingFireTicks", at = @At(value = "HEAD"), cancellable = true)
    public void setRemainingFireTicks(int p_20269_, CallbackInfo ci) {
        if (((Entity) (Object) this) instanceof Player player && data.isActiveAbility(player, "lord_elements"))
            ci.cancel();
    }

    @Inject(method = "getRemainingFireTicks", at = @At(value = "HEAD"), cancellable = true)
    public void getRemainingFireTicks(CallbackInfoReturnable<Integer> ci) {
        if ((((Entity) (Object) this) instanceof Player player && data.isActiveAbility(player, "lord_elements"))) {
            ci.setReturnValue(0);
            ci.cancel();
        }
    }

    @Inject(method = "setAirSupply", at = @At(value = "HEAD"), cancellable = true)
    public void setAirSupply(int amount, CallbackInfo ci) {
        if ((((Entity) (Object) this) instanceof Player player && Minecraft.getInstance().getConnection() != null && data.isActiveAbility(player, "lord_elements")))
            ci.cancel();
    }
}
