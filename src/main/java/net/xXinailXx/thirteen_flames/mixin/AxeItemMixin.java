package net.xXinailXx.thirteen_flames.mixin;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.context.UseOnContext;
import net.xXinailXx.thirteen_flames.client.progress.ProgressManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AxeItem.class)
public class AxeItemMixin {
    @Inject(method = "useOn", at = @At(value = "HEAD"), cancellable = true)
    public void useOn(UseOnContext use, CallbackInfoReturnable<InteractionResult> cir) {
        if (!ProgressManager.isAllowStatUsage(ProgressManager.ProgressType.MINING, use.getItemInHand())) {
            cir.setReturnValue(InteractionResult.SUCCESS);
            cir.cancel();
        }
    }
}
