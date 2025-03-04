package net.xXinailXx.thirteen_flames.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import daripher.skilltree.client.widget.SkillButton;
import net.minecraft.client.Minecraft;
import net.xXinailXx.thirteen_flames.data.IData;
import net.xXinailXx.thirteen_flames.data.Data;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SkillButton.class)
public class SkillButtomMixin {
    @Unique private static final IData.IGuiLevelingData guiLevelingData = new Data.GuiLevelingData();

    @Inject(method = "renderButton", at = @At(value = "INVOKE", target = "Ldaripher/skilltree/client/widget/SkillButton;blit(Lcom/mojang/blaze3d/vertex/PoseStack;IIIIFFIIII)V", ordinal = 3), cancellable = true)
    public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (Minecraft.getInstance().player == null)
            return;

        if (guiLevelingData.isPlayerScreen()) {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            ci.cancel();
        }
    }
}
