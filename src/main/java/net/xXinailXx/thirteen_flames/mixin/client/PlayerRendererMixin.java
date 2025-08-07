package net.xXinailXx.thirteen_flames.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.Tag;
import net.xXinailXx.enderdragonlib.capability.PlayerCapManager;
import net.xXinailXx.thirteen_flames.init.EffectRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public class PlayerRendererMixin {
    @Inject(method = "render(Lnet/minecraft/client/player/AbstractClientPlayer;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "HEAD"), cancellable = true)
    public void render(AbstractClientPlayer player, float p_117789_, float p_117790_, PoseStack p_117791_, MultiBufferSource p_117792_, int p_117793_, CallbackInfo ci) {
        if (PlayerCapManager.getTimer(player, "tf_mask_demiurg_dematerialization") > 0)
            ci.cancel();
    }
}
