package net.xXinailXx.thirteen_flames.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.player.Player;
import net.xXinailXx.thirteen_flames.item.flame.MoonBow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(method = "bobHurt", at = @At("HEAD"), cancellable = true)
    public void bobHurt(PoseStack stack, float partialTicks, CallbackInfo ci) {
        Player player = Minecraft.getInstance().player;

        if (player != null && Objects.equals(player.getLastDamageSource(), MoonBow.SUCC))
            ci.cancel();
    }
}
