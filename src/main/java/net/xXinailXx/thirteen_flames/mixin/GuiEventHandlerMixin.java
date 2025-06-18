package net.xXinailXx.thirteen_flames.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.gui.events.GuiEventHandler;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.xXinailXx.thirteen_flames.client.gui.scroll.ScrollScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiEventHandler.class)
public class GuiEventHandlerMixin {
    @Inject(method = "onGuiInit", at = @At(value = "HEAD"), cancellable = true, remap = false)
    public void onGuiInit(Screen screen, CallbackInfo ci) {
        if (screen instanceof ScrollScreen)
            ci.cancel();
    }

    @Inject(method = "onGuiOpen", at = @At(value = "HEAD"), cancellable = true, remap = false)
    public void onGuiOpen(Screen screen, CallbackInfo ci) {
        if (screen instanceof ScrollScreen)
            ci.cancel();
    }

    @Inject(method = "onDrawBackgroundPost", at = @At(value = "HEAD"), cancellable = true, remap = false)
    public void onDrawBackgroundPost(Screen screen, PoseStack poseStack, CallbackInfo ci) {
        if (screen instanceof ScrollScreen)
            ci.cancel();
    }

    @Inject(method = "onDrawForeground", at = @At(value = "HEAD"), cancellable = true, remap = false)
    public void onDrawForeground(AbstractContainerScreen<?> screen, PoseStack poseStack, int mouseX, int mouseY, CallbackInfo ci) {
        if (screen instanceof ScrollScreen)
            ci.cancel();
    }

    @Inject(method = "onDrawScreenPost", at = @At(value = "HEAD"), cancellable = true, remap = false)
    public void onDrawScreenPost(Screen screen, PoseStack poseStack, int mouseX, int mouseY, CallbackInfo ci) {
        if (screen instanceof ScrollScreen)
            ci.cancel();
    }
}
