package net.xXinailXx.thirteen_flames.mixin.client;

import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.xXinailXx.thirteen_flames.item.flame.MoonBow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {
    @Inject(method = "getOverlayCoords", at = @At("HEAD"), cancellable = true)
    private static void getOverlayCoords(LivingEntity entity, float v, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(OverlayTexture.pack(OverlayTexture.u(v), OverlayTexture.v((entity.hurtTime > 0 || entity.deathTime > 0) && entity.getLastDamageSource() != MoonBow.SUCC)));
        cir.cancel();
    }
}
