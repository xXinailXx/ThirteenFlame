package net.xXinailXx.thirteen_flames.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import it.hurts.sskirillss.relics.client.renderer.tiles.ResearchingTableRenderer;
import it.hurts.sskirillss.relics.tiles.ResearchingTableTile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.xXinailXx.thirteen_flames.init.ItemRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ResearchingTableRenderer.class)
public class ResearchingTableRendererMixin {
    @Inject(method = "render(Lit/hurts/sskirillss/relics/tiles/ResearchingTableTile;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lcom/mojang/math/Quaternion;)V"))
    public void render(ResearchingTableTile tileEntity, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay, CallbackInfo ci) {
        if (tileEntity.getStack().is(ItemRegistry.SHIELD_RONOSA.get()))
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(180));
    }
}
