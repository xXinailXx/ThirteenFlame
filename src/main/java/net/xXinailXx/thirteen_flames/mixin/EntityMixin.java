package net.xXinailXx.thirteen_flames.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.xXinailXx.thirteen_flames.data.Data;
import net.xXinailXx.thirteen_flames.data.IData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
    @Shadow private int remainingFireTicks;
    @Unique private IData.IAbilitiesData data = new Data.AbilitiesData.Utils();

    @Inject(method = "setSecondsOnFire", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/ProtectionEnchantment;getFireAfterDampener(Lnet/minecraft/world/entity/LivingEntity;I)I"), remap = false, cancellable = true)
    public void setSecondsOnFire(int i, CallbackInfo ci) {
        if (data.isActiveAbility("lord_elements") && ((LivingEntity) (Object) this) instanceof Player) {
            ci.cancel();
        }
    }

    @Overwrite
    public void setRemainingFireTicks(int i) {
        if (!data.isActiveAbility("lord_elements") && !(((Entity) (Object) this) instanceof Player)) {
            this.remainingFireTicks = i;
        }
    }

    @Overwrite
    public int getRemainingFireTicks() {
        if (data.isActiveAbility("lord_elements") && ((LivingEntity) (Object) this) instanceof Player) {
            return 0;
        } else {
            return this.remainingFireTicks;
        }
    }
}
